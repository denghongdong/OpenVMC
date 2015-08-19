#ifndef _ITS_VEHICLE_TYPE_REC_H
#define _ITS_VEHICLE_TYPE_REC_H
#include "PlateType.h"

#define  MAX_VEHICLE_NUM  (10)
#define  MAX_FACE_NUM     (10)

#define  OK           (0)     //初始化正确
#define  DATA_OUT     (-1)    //SDK过期


/***************车身颜色******************/
typedef enum
{
	E_VEHICLE_COLOR_NOT,//未知
	E_VEHICLE_RED,      //红色
	E_VEHICLE_GREEN,    //绿色
	E_VEHICLE_BLUE,     //蓝色
	E_VEHICLE_PINK,     //粉色
	E_VEHICLE_BROWN,    //棕色
	E_VEHICLE_YELLOW,   //黄色
	E_VEHICLE_WHITE,    //白色
	E_VEHICLE_BLACK,    //黑色
	E_VEHICLE_GRAY,     //灰色
	E_VEHICLE_CYAN,     //青色
}E_Vehicle_Color;


typedef enum 
{
	E_RGB,
	E_BGR,
	E_GRAY,
}V_ImageType;  //图片格式

typedef struct
{
	int       iImageHeight;
	int       iImageWidth;
	V_ImageType eType;
	unsigned char *pImageData;
}V_Image; //图像格式


typedef struct  
{
	int iLeft;
	int iTop;
	int iRight;
	int iBottom;
}S_Rect;


typedef struct
{
	char tempVehicleType[1024];    //车辆类型
	float fConfdence;              //车辆类型置信度
	PlateInfor plateInfor;         //车牌信息
	S_Rect plateRect;              //车牌位置
	S_Rect pVehicleRect;        //车辆位置
	int iVehicleSubModel;          //索引
	E_Vehicle_Color eVehicleColor1;//车身主颜色
	E_Vehicle_Color eVehicleColor2;//车身辅颜色
	
}ITS_Vehicle_Result_Single;


typedef struct
{
	ITS_Vehicle_Result_Single tempResult[MAX_VEHICLE_NUM];
	int iNum;
	float fLprTime;
	float fVtrTime;
}ITS_Vehicle_Result;

//extern "C" __declspec (dllexport) void  ITS_ThreadInit();
//
//extern "C" __declspec (dllexport) void * ITS_VehicleRecInit(char* modePath, int& iInitFlag);//modePath为model文件夹的路径名
//
//extern "C" __declspec (dllexport) int ITS_VehicleTypeRec(void* pInstance,V_Image* pVImage,ITS_Vehicle_Result* pResult);
//
//extern "C" __declspec (dllexport) void ITS_VehicleRecRelease(void* pInstance);





#endif