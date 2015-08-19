
#include "com_seasbase_video_io_VideoReader.h"
#include "windows.h"
#include <cv.h>
#include <highgui.h>
#include "VehicleProc.h"


/*
 * Class:     com_seasbase_video_io_VideoReader
 * Method:    DiffProc
 * Signature: (Lcom/googlecode/javacv/cpp/opencv_core/IplImage;IILjava/util/List;)Ljava/lang/String;
 */
//外部声明
typedef void (*ThreadInit)();
typedef void* (*VehicleRecInit)(char*,int&);
typedef void (*VehicleRecRelease)(void*);
//jstring to char* 
char* jstringTostring(JNIEnv* env, jstring jstr) 
{ 
	char* rtn = NULL; 
	jclass clsstring = env->FindClass("java/lang/String"); 
	jstring strencode = env->NewStringUTF("utf-8"); 
	jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B"); 
	jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode); 
	jsize alen = env->GetArrayLength(barr); 
	jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE); 
	if (alen > 0) 
	{ 
		rtn = (char*)malloc(alen + 1); 
		memcpy(rtn, ba, alen); 
		rtn[alen] = 0; 
	} 
	env->ReleaseByteArrayElements(barr, ba, 0); 
	return rtn; 
} 
jstring WindowsTojstring( JNIEnv* env, char* str )

{
	jstring rtn = 0;
	int slen = strlen(str);
	unsigned short* buffer = 0;
	if( slen == 0 )
		rtn = env->NewStringUTF( str ); 
	else
	{
		int length = MultiByteToWideChar( CP_ACP, 0, (LPCSTR)str, slen, NULL, 0 );
		buffer = (unsigned short *)malloc( length*2 + 1 );
		if( MultiByteToWideChar( CP_ACP, 0, (LPCSTR)str, slen, (LPWSTR)buffer, length ) >0 )
			rtn = env->NewString((jchar*)buffer, length );
	}
	if( buffer )
		free( buffer );
	return rtn;
}
 
CVehicleProc vehproc;
JNIEXPORT  void JNICALL Java_com_seasbase_video_io_VideoReader_DiffProc(JNIEnv * env, jobject obj, jstring jo,jint thd,jint nframes)
{
	jclass jovideo = env->GetObjectClass(obj);
	jmethodID appendInfo = env->GetMethodID(jovideo,"appendInfo", "(Lcom/seasbase/video/io/Info;)V");//关键代码行

	jclass jinfo = env->FindClass("com/seasbase/video/io/Info");
	jmethodID initInfo = env->GetMethodID(jinfo, "<init>","(II[BIIIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	//调用代码
	HMODULE dlh = NULL;
	char buf[256]={0};
	//dlh=LoadLibrary("VehicleTypeRecognition_D.dll");
	//dlh=LoadLibrary("HelloWorld.dll");
	if (!(dlh=LoadLibrary("VehicleTypeRecognition_D.dll")))      
	{
		printf("LoadLibrary() failed: %d\n", GetLastError()); 
	}

	ThreadInit threadInit;
	threadInit = (ThreadInit)GetProcAddress(dlh, "ITS_ThreadInit");//ITS_ThreadInit(); 进程启动时调用
	/*threadInit = (ThreadInit)GetProcAddress(dlh, "PrintHelloWord");*/
	(*threadInit)();
	
	VehicleRecInit vehicleRecInit;
	int iInitFlag;
	char* pathvar = getenv("ALG_DIR");
	sprintf(buf,"%smodel",pathvar);
	char* modePath = buf;//	..\\bin\\model
	vehicleRecInit = (VehicleRecInit)GetProcAddress(dlh, "ITS_VehicleRecInit");
	vehproc.pInstance = (*vehicleRecInit)(modePath, iInitFlag);//初始化操作
	char* fpath =NULL;
	fpath = jstringTostring(env,jo);
	
	 CvCapture * capture = cvCreateFileCapture(fpath);
	long nbFrames = (long) cvGetCaptureProperty(capture, CV_CAP_PROP_FRAME_COUNT);	
	
	if( !capture )
	{
		fprintf(stderr,"Could not initialize capturing...\n");	
	}	
	IplImage* pSrcImg =NULL;
	////////////////////////////////////////////////////
	while (cvGrabFrame(capture) != 0 && (vehproc.pFrame = cvRetrieveFrame(capture)) != NULL) 
	{	
		nframes++;
		vehproc.nfrnum++;
		/*if(vehproc.nfrnum>40)
		{
			if(vehproc.nfrnum%5!=0)
				continue;
		}*/
		if (NULL==pSrcImg)
		{
			pSrcImg =cvCreateImage(cvSize(vehproc.pFrame->width,vehproc.pFrame->height),IPL_DEPTH_8U,3);
		}
		cvCopyImage(vehproc.pFrame,pSrcImg);
		
		
		vehproc.VehiclePreProc(pSrcImg);
		jobject info;
		jstring jnumber;jstring jtype;jstring jcolor;
		for (int j = 0; j < vehproc.vehCnt; j++)//
		{
			/*threadInit = (ThreadInit)GetProcAddress(dlh, "PrintHelloWord");
			(*threadInit)();*/
			IplImage * pOutput = vehproc.vehinfo[j].vehicleImg;//cvCreateImage(cvSize(vehproc.pFrame->width,vehproc.pFrame->height),IPL_DEPTH_8U,3);
			//cvCopyImage(vehproc.pFrame,pOutput);vehproc.vehinfo[j].plateImg;
			sprintf(buf,"E:\\aaaa\\%d-%d.jpg",nframes,j);
			cvSaveImage(buf,pOutput);
			CvMat* mat = cvEncodeImage(".jpg",pOutput);
			jbyteArray jarrRV =env->NewByteArray(mat->cols);
			/*env->NewFloatArray()
			env->NewFloatArray()
			env->NewFloatArray()*/
		    env->SetByteArrayRegion(jarrRV,0,mat->cols,(const jbyte *)mat->data.ptr);
			cvReleaseMat(&mat);	
			jnumber = WindowsTojstring(env,vehproc.vehinfo[j].platenumber);
			jtype = WindowsTojstring(env,vehproc.vehinfo[j].tempVehicleType);
			jcolor= WindowsTojstring(env,vehproc.vehinfo[j].eVehicleColor);
			//jnumber = env->NewStringUTF(vehproc.vehinfo[j].platenumber);
			//jtype = env->NewStringUTF(vehproc.vehinfo[j].tempVehicleType);
			//jbyteArray jarrRV =env->NewByteArray(1);
			/*jnumber = env->NewStringUTF("fde");
			jtype = env->NewStringUTF("bbb");
			jcolor = env->NewStringUTF("ccab");*/
			//黑AU5881
			if (strcmp(vehproc.vehinfo[j].platenumber,"黑AU5881")==0)
			{
				system("pause");
			}
			info = env->NewObject(jinfo,initInfo,vehproc.nfrnum,j,jarrRV,vehproc.vehinfo[j].pointLT.x,vehproc.vehinfo[j].pointLT.y,vehproc.vehinfo[j].width,vehproc.vehinfo[j].height,jnumber,jtype,jcolor);
			//info = env->NewObject(jinfo,initInfo,vehproc.nfrnum,j,jarrRV,100,4,5,6,jnumber,jtype,jcolor);
			env->CallObjectMethod(obj,appendInfo,info);
			env->DeleteLocalRef(info);
			env->DeleteLocalRef(jarrRV);
			cvReleaseImage(&pOutput);
		}
		
//#ifdef DEBUG_OUTPUT_IMAGE
	//sprintf(buf,"E:\\aaaa\\%da.jpg",nframes);
	cvSaveImage(buf,vehproc.pFrame);
	//sprintf(buf,"E:\\aaaa\\%db.jpg",nframes);
	cvSaveImage(buf,pSrcImg);
//#endif
	}
	jstring jnumber;
	jstring jtype;
	jstring jcolor;
	jnumber = env->NewStringUTF("aaaaa");
	jtype = env->NewStringUTF("bbbbb");
	jcolor = env->NewStringUTF("ccccc");
	jbyteArray jarrRV1 =env->NewByteArray(1);////
	jobject info1 = env->NewObject(jinfo,initInfo,-1,-1,jarrRV1,-1,2,3,4,jnumber,jtype,jcolor);
	env->CallObjectMethod(obj,appendInfo,info1);
	env->DeleteLocalRef(info1);
	env->DeleteLocalRef(jarrRV1);

  env->DeleteLocalRef(jinfo);
  cvReleaseImage(&pSrcImg);
  cvReleaseCapture(&capture); //释放视频空间  
  VehicleRecRelease vehicleRecRelease;
  vehicleRecRelease = (VehicleRecRelease)GetProcAddress(dlh, "ITS_VehicleRecRelease");
  (*vehicleRecRelease)(vehproc.pInstance);
	return;

}
JNIEXPORT void JNICALL Java_com_seasbase_video_io_VideoReader_Release(JNIEnv *env, jobject obj)
{
	//ITS_VehicleRecRelease(pInstance); //释放
	//调用代码
	/*HMODULE dlh = NULL;
	if (!(dlh=LoadLibrary("VehicleTypeRecognition_D.dll")))      
	{
	printf("LoadLibrary() failed: %d\n", GetLastError()); 
	}

*/
}


