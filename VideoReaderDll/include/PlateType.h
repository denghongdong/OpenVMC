#ifndef PLATE_TYPE_H
#define PLATE_TYPE_H




/***************车牌颜色******************/
typedef enum
{
	E_PLATE_COLOR_NOT,  //未知
	E_PLATE_COLOR_BLUE,
	E_PLATE_COLOR_BLACK,
	E_PLATE_COLOR_YELLOW,
	E_PLATE_COLOR_WHITE,
}E_Plate_Color; 



/***************车牌类型******************/
typedef enum
{
	E_PLATE_TYPE_NOT,          //未知
	E_PLATE_TYPE_NORMAL_BLUE,  //普通蓝牌
	E_PLATE_TYPE_NORMAL_BLACK, //普通黑牌
	E_PLATE_TYPE_NORMAL_YELLOW,//普通单层黄牌
	E_PLATE_TYPE_DOUBLE_YELLOW,//双层黄牌
	E_PLATE_TYPE_POLICE,       //白色警牌
	E_PLATE_TYPE_WJ,           //白色武警
	E_PLATE_TYPE_ARMY,         //白色军牌
	E_PLATE_TYPE_GANG,         //港牌
	E_PLATE_TYPE_NONG,         //农用车牌
}E_Plate_Type; 



typedef struct
{
	E_Plate_Color ePlateColor;
	E_Plate_Type  ePlateType;
	char platenumber[20];         //车牌号码
}PlateInfor;




#endif