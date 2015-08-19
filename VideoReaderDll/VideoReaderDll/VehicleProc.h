#pragma once
#include <cv.h>
#include <highgui.h>
#include "minmax.h"  //注意这个文件只能放在这个位置包含，否则会出错 提示找不到 min max
using namespace std;
#define  MAX_VEHICLE_NUM  (10)
typedef struct
{
  CvPoint pointLT;            //车牌位置左上
  int width;          //车牌宽度
  int height;         //车牌高度
  IplImage* plateImg;        //车牌图像
  IplImage* vehicleImg;        //车辆图像
  char tempVehicleType[1024]; //车辆类型
  char platenumber[20];         //车牌号码
  char* eVehicleColor;      //车身颜色
} VehicleInfo;
class CVehicleProc
{
public:
	CVehicleProc(void);
	~CVehicleProc(void);
	void VehiclePreProc(IplImage* src);

	VehicleInfo vehinfo[MAX_VEHICLE_NUM];
	int vehCnt;
	void* pInstance;
	int nfrnum;
	IplImage * pFrame ;
	

};