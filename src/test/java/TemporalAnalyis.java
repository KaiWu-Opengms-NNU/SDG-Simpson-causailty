import com.opengms.wukai.pojo.Result.Spatial.ArcmapForSpace;
import com.opengms.wukai.pojo.Result.Prioritize.TemporalForResult;
import com.opengms.wukai.tool.OperateExcel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemporalAnalyis {
    public static String TemporalBasePath="G:/WukaiBag/Year2023/sustainableofpaper2023/result3/0830/SameInteraction";
    public static String TemporalOutPath="G:/WukaiBag/Year2023/sustainableofpaper2023/result3/0830/result_temporal_difference";

    public static void main(String[] args) throws IOException {
        OperateExcel operateExcel = new OperateExcel();
        File dir = new File(TemporalBasePath);
        List<File>  AllFileList=operateExcel.getAllFile(dir);
        List<List<ArcmapForSpace>> SameInteractionList=new ArrayList<>();
        List<String> SameInteractionListName=new ArrayList<>();
        //批量读入文件夹下的文件
        for(int i=0;i<AllFileList.size();i++){
            SameInteractionListName.add(AllFileList.get(i).getName());
            List<List<String>> TempData=operateExcel.readExcel2(AllFileList.get(i));
            List<ArcmapForSpace> TempArcmapForSpace=new ArrayList<>();
            if(TempData.size()>1){
                //载入数据
                for(int Ti=1;Ti<TempData.size();Ti++){
                    ArcmapForSpace TempOneArcmap= new ArcmapForSpace();
                    TempOneArcmap.setAreaName(TempData.get(Ti).get(0));
                    TempOneArcmap.setAreaAb(TempData.get(Ti).get(1));
                    TempOneArcmap.setAreaID(TempData.get(Ti).get(2));
                    TempOneArcmap.setScale(Integer.parseInt(TempData.get(Ti).get(3)));
                    TempOneArcmap.setGoalX(TempData.get(Ti).get(4));
                    TempOneArcmap.setGoalY(TempData.get(Ti).get(5));
                    TempOneArcmap.setTargetX(TempData.get(Ti).get(6));
                    TempOneArcmap.setTargetY(TempData.get(Ti).get(7));
                    TempOneArcmap.setInteraction(TempData.get(Ti).get(9));
                    TempOneArcmap.setStrengthAll(Double.parseDouble(TempData.get(Ti).get(10)));
                    TempOneArcmap.setStrengthAllRSquared(Double.parseDouble(TempData.get(Ti).get(11)));
                    TempOneArcmap.setStrengthWithDirection(Double.parseDouble(TempData.get(Ti).get(12)));
                    TempOneArcmap.setOriginalType(Integer.parseInt(TempData.get(Ti).get(13)));
                    TempOneArcmap.setOriginalStrength(Double.parseDouble(TempData.get(Ti).get(14)));
                    TempOneArcmap.setChangeType(Integer.parseInt(TempData.get(Ti).get(15)));
                    TempOneArcmap.setChangeStrength(Double.parseDouble(TempData.get(Ti).get(16)));
                    TempOneArcmap.setPairLevel(Integer.parseInt(TempData.get(Ti).get(17)));
                    TempOneArcmap.setInteractionVar(Double.parseDouble(TempData.get(Ti).get(18)));
                    TempOneArcmap.setStrengthVar(Double.parseDouble(TempData.get(Ti).get(19)));
                    TempOneArcmap.setCountryInteraction(TempData.get(Ti).get(20));
                    TempOneArcmap.setCountryNumber(Integer.parseInt(TempData.get(Ti).get(21)));
                    TempOneArcmap.setRegionInteraction(TempData.get(Ti).get(22));
                    TempOneArcmap.setRegionNumber(Integer.parseInt(TempData.get(Ti).get(23)));
                    TempOneArcmap.setWorldInteraction(TempData.get(Ti).get(24));
                    TempOneArcmap.setWorldNumber(Integer.parseInt(TempData.get(Ti).get(25)));
                    TempArcmapForSpace.add(TempOneArcmap);
                }
            }
            SameInteractionList.add(TempArcmapForSpace);
        }
        //载入数据完成，找出那些指标存在辛普森效应
        List<TemporalForResult> TempSameGoalList=new ArrayList<>();
        for(int xi=1;xi<=17;xi++){
            for(int yi=1;yi<=17;yi++){
                for(int Fi=0;Fi<SameInteractionListName.size();Fi++){
                    String[] NameList=String.valueOf(SameInteractionListName.get(Fi)).split("_");
                    int Data_X=Integer.parseInt(NameList[0]);
                    int Data_Y=Integer.parseInt(NameList[1]);
                    int Data_TemporalForResult=Integer.parseInt(NameList[2].substring(0,1));
                    if(xi==Data_X&&yi==Data_Y){
                        //
                        if (Data_TemporalForResult==1){
                            for(int Ci=0;Ci<SameInteractionList.get(Fi).size();Ci++){
                                //只有再链表等于零的时候才置入新的行，否则就在旧行上置入
                                TemporalForResult temporalForResult=new TemporalForResult();
                                if(TempSameGoalList.size()==0){
                                    temporalForResult.setAreaName(SameInteractionList.get(Fi).get(Ci).getAreaName());
                                    temporalForResult.setAreaAb(SameInteractionList.get(Fi).get(Ci).getAreaAb());
                                    temporalForResult.setAreaID(SameInteractionList.get(Fi).get(Ci).getAreaID());
                                    temporalForResult.setScale(SameInteractionList.get(Fi).get(Ci).getScale());
                                    temporalForResult.setGoalX(xi);
                                    temporalForResult.setGoalY(yi);
                                    temporalForResult.SetOneYearScale(SameInteractionList.get(Fi).get(Ci));
                                    TempSameGoalList.add(temporalForResult);
                                }else{
                                    int InsertIndex=-1;
                                    for(int Ti=0;Ti<TempSameGoalList.size();Ti++){
                                        if(TempSameGoalList.get(Ti).getGoalX()==Data_X&&TempSameGoalList.get(Ti).getGoalY()==Data_Y){
                                            if(TempSameGoalList.get(Ti).getAreaID().equals(SameInteractionList.get(Fi).get(Ci).getAreaID())){
                                                InsertIndex=Ti;
                                                break;
                                            }
                                        }
                                    }
                                    //判断已存在还是未存在
                                    if(InsertIndex==-1){
                                        temporalForResult.setAreaName(SameInteractionList.get(Fi).get(Ci).getAreaName());
                                        temporalForResult.setAreaAb(SameInteractionList.get(Fi).get(Ci).getAreaAb());
                                        temporalForResult.setAreaID(SameInteractionList.get(Fi).get(Ci).getAreaID());
                                        temporalForResult.setScale(SameInteractionList.get(Fi).get(Ci).getScale());
                                        temporalForResult.setGoalX(xi);
                                        temporalForResult.setGoalY(yi);
                                        temporalForResult.SetOneYearScale(SameInteractionList.get(Fi).get(Ci));
                                        TempSameGoalList.add(temporalForResult);
                                    }else{
                                        TempSameGoalList.get(InsertIndex).SetOneYearScale(SameInteractionList.get(Fi).get(Ci));
                                    }
                                }

                            }
                        }
                        if (Data_TemporalForResult==3){
                            for(int Ci=0;Ci<SameInteractionList.get(Fi).size();Ci++){
                                //只有再链表等于零的时候才置入新的行，否则就在旧行上置入
                                TemporalForResult temporalForResult=new TemporalForResult();
                                if(TempSameGoalList.size()==0){
                                    temporalForResult.setAreaName(SameInteractionList.get(Fi).get(Ci).getAreaName());
                                    temporalForResult.setAreaAb(SameInteractionList.get(Fi).get(Ci).getAreaAb());
                                    temporalForResult.setAreaID(SameInteractionList.get(Fi).get(Ci).getAreaID());
                                    temporalForResult.setScale(SameInteractionList.get(Fi).get(Ci).getScale());
                                    temporalForResult.setGoalX(xi);
                                    temporalForResult.setGoalY(yi);
                                    temporalForResult.SetThreeYearScale(SameInteractionList.get(Fi).get(Ci));
                                    TempSameGoalList.add(temporalForResult);
                                }else{
                                    int InsertIndex=-1;
                                    for(int Ti=0;Ti<TempSameGoalList.size();Ti++){
                                        if(TempSameGoalList.get(Ti).getGoalX()==Data_X&&TempSameGoalList.get(Ti).getGoalY()==Data_Y){
                                            if(TempSameGoalList.get(Ti).getAreaID().equals(SameInteractionList.get(Fi).get(Ci).getAreaID())){
                                                InsertIndex=Ti;
                                                break;
                                            }
                                        }
                                    }
                                    //判断已存在还是未存在
                                    if(InsertIndex==-1){
                                        temporalForResult.setAreaName(SameInteractionList.get(Fi).get(Ci).getAreaName());
                                        temporalForResult.setAreaAb(SameInteractionList.get(Fi).get(Ci).getAreaAb());
                                        temporalForResult.setAreaID(SameInteractionList.get(Fi).get(Ci).getAreaID());
                                        temporalForResult.setScale(SameInteractionList.get(Fi).get(Ci).getScale());
                                        temporalForResult.setGoalX(xi);
                                        temporalForResult.setGoalY(yi);
                                        temporalForResult.SetThreeYearScale(SameInteractionList.get(Fi).get(Ci));
                                        TempSameGoalList.add(temporalForResult);
                                    }else{
                                        TempSameGoalList.get(InsertIndex).SetThreeYearScale(SameInteractionList.get(Fi).get(Ci));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //开始分析
        System.out.println("Success");
        for(int i=0;i<TempSameGoalList.size();i++){
            TempSameGoalList.get(i).JudgeAndSetResult();
        }
        if(TempSameGoalList.size()>0){
            operateExcel.writeExcel(TemporalOutPath,"TemporalDifferenceList",TemporalForResult.class, TempSameGoalList);
        }
    }
}
