package edu.sdust.insapp.utils;

/**
 * Created by Administrator on 2017/11/17.
 */

public class UrlConstant {
    public static String BASE_URL = "http://120.27.37.112:8088/pet-equ-mai-sys";
    //public static String BASE_URL = "http://123.56.221.97:8080/pet-equ-mai-sys";
    //public static String BASE_URL = "http://10.0.2.2:8080/pet-equ-mai-sys";
    //public static String BASE_URL = "http://192.168.137.1:8080/pet-equ-mai-sys";
    //public static String BASE_URL = "http://192.168.81.83:8080/pet-equ-mai-sys";
    //public static String BASE_URL = "http://192.168.0.207:8080/pet-equ-mai-sys";//
    //public static String BASE_URL = "http://192.168.0.138:8080/pet-equ-mai-sys";//
    //public static String BASE_URL = "http://192.168.81.97:8080/pet-equ-mai-sys";
    //public static String BASE_URL = "http://192.168.81.77:8080/pet-equ-mai-sys";
    //public static String BASE_URL="http://192.168.23.1:8080/pet-equ-mai-sys";
    //public static String BASE_URL = "http://192.168.191.1:8080/pet-equ-mai-sys";
    public static final String loginURL = BASE_URL+"/app/user/login";
    public static final String dispatchOrderURL = BASE_URL+"/app/ins/downloadDispatchOrder";
    public static final String eqptTagURL = BASE_URL+"/EqptAccnt/getEqptTag";
    public static final String meaPointDataURL = BASE_URL+"/app/ins/downloadMeaPointData";
    public static final String uploadURL = BASE_URL+"/app/ins/uploadInsCompOrderRequest";
    public static final String uploadElecURL = BASE_URL+"/app/ins/uploadElecInsCompOrderRequest";
    public static final String uploadElecEncloURL = BASE_URL+"/app/ins/uploadElecInspEncloRequest";
    public static final String getSecHarmAnalysisAppURL = BASE_URL+"/app/eqptRepairDisOrderList/getSecHarmAnalysisApp";//获取安全危害分析
    public static final String getSecMeasAppURL = BASE_URL+"/app/eqptRepairDisOrderList/getSecMeasApp";//获取安全措施
    public static final String getAllGroStaffAppURL = BASE_URL+"/app/eqptRepairDisOrderList/getAllGroStaffApp";//获取对应班组的人员
    public static final String disSecDisOrderAppURL = BASE_URL+"/app/eqptRepairDisOrderList/disSecDisOrderApp";//二次派工派工
    public static final String upload = BASE_URL + "/app/fault/uploadEqptFault";
    public static final String noupLoad =BASE_URL +"/app/fault/uploadNonEqptFault";
    public static final String problemPhenomenonURL = BASE_URL+"/NotEquipmentProblem/ListAllProblemphenomenon";
    public static final String eqptfaultpositionURl = BASE_URL+"/EquipmentProblem/getEqptfaultpositionTree";
    public static final String inspproblemscenetreatmentmodeURL = BASE_URL+"/NotEquipmentProblem/ListAllInspproblemscenetreatmentmode";
    public static final String getFaultcauseAppURL = BASE_URL + "/app/eqptRepairDisOrderList/getFaultcauseApp";
    public static final String getFaulttreatmentmeasAppURL = BASE_URL + "/app/eqptRepairDisOrderList/getFaulttreatmentmeasApp";//故障处理措施
    public static final String getFaultmechanismTreeAppURL = BASE_URL + "/app/eqptRepairDisOrderList/getFaultmechanismTreeApp";//故障机理
    public static final String getEqptFaultPositionTreeByEqptCatgAppURL = BASE_URL + "/app/eqptRepairDisOrderList/getEqptFaultPositionTreeByEqptCatgApp";//故障部位
    public static final String addProFaultPositionAppURL = BASE_URL + "/app/eqptRepairDisOrderList/addProFaultPositionApp";//添加设备故障部位
    public static final String listAllViEqptProblemFaultPositionAppURL = BASE_URL + "/app/eqptRepairDisOrderList/listAllViEqptProblemFaultPositionApp";//获取设备问题任务对应设备故障部位
    public static final String updateURL = BASE_URL + "/app/update/getUpdateInfo";
    public static final String locationURL = BASE_URL+"/location/add";
    public static final String elecLocationURL = BASE_URL+"/location/elecAdd";
    public static final String userInformationURL = BASE_URL+"/app/user/getUserInfo";
    public static final String ectg = BASE_URL+"/EqptCatgFaultPosition/getEqptCatgTree";
    public static final String getDeviceTreeURL = BASE_URL+"/BrakeOrderForDispa/getDeviceTree";//获取设备树
    public static final String listEqptFault = BASE_URL+"/app/fault/listEqptFaultProcessing";
    public static final String listNonEqptFault = BASE_URL+"/app/fault/listNonEqptFaultProcessing";
    public static final String listeqptorder = BASE_URL+"/app/eqptRepairDisOrderList/getEqptRepairDisOrderList";
    public static final String Listeqptproblem = BASE_URL+"/app/eqptRepairDisOrderList/listAllViEqptRepairTaskProblemApp";
    public static final String updateEqptRepairTaskComAppURL = BASE_URL + "/app/eqptRepairDisOrderList/updateEqptRepairTaskComApp";//保存设备问题任务
    public static final String updatenonEqptRepairTaskComAppURL = BASE_URL + "/app/nonEqptRepairDisOrderList/updatenonEqptRepairTaskComApp";//保存非设备问题任务
    public static final String uploadEqptRepairWorkTicketAppURL = BASE_URL + "/app/eqptRepairDisOrderList/uploadEqptRepairWorkTicketApp";
    public static final String isUploadedEqptRepairWorkTicketAppURL = BASE_URL + "/app/eqptRepairDisOrderList/isUploadedEqptRepairWorkTicketApp";
    public static final String comSecDisOrderAppURL = BASE_URL + "/app/eqptRepairDisOrderList/comSecDisOrderApp";
    public static final String eqptproblem = BASE_URL+"/app/eqptRepairDisOrderList/AllComEqptRepairDisOrderProblem";
    public static final String nonListeqptorder = BASE_URL+"/app/nonEqptRepairDisOrderList/getNonEqptRepairDisOrderList";
    public static final String noneqptproblem = BASE_URL+"/app/nonEqptRepairDisOrderList/listNonAllViEqptRepairTaskProblemApp";
    public static final String listAllViEqptProblemWiOuPageURL = BASE_URL+"/ViEqptRepairDisOrder/listAllViEqptProblemWiOuPage";//查找所有未派工的设备问题
    public static final String listAllViNonEqptProblemWiOuPageURL = BASE_URL+"/ViNonEqptRepairDisOrder/listAllViNonEqptProblemWiOuPage";//查找所有未派工的非设备问题
    public static final String getOverParGroupTreeUrl = BASE_URL+"/InspRoute/getOverParGroupTree";//获取班组树
    public static final String addViEqptRepairDisOrderAppURL = BASE_URL+"/app/eqptRepairDisOrderList/addViEqptRepairDisOrderApp";//新建设备维修派工单
    public static final String addViNonEqptRepairDisOrderAppURL = BASE_URL+"/app/nonEqptRepairDisOrderList/addViNonEqptRepairDisOrderApp";//新建非设备维修派工单
    public static final String weatherFillinEqptProblemTaskAppURL = BASE_URL+"/app/eqptRepairDisOrderList/weatherFillinEqptProblemTaskApp";//判断设备问题任务是否填写
    public static final String weatherFillinNonEqptProblemTaskAppURL = BASE_URL+"/app/nonEqptRepairDisOrderList/weatherFillinNonEqptProblemTaskApp";//判断非设备问题任务是否填写
    public static final String uploadRepairDisOrderAnnexAppURL = BASE_URL+"/app/eqptRepairDisOrderList/uploadRepairDisOrderAnnexApp";//上传维修派工单附件
    public static final String getEqptAccntTreeURL = BASE_URL+"/app/fault/getEqptAccntTree";//获取设备台账树
    public static final String listAllViKeepWorkWorkRecordAppURL = BASE_URL+"/app/keepwork/listAllViKeepWorkWorkRecordApp";//列出所以保运工作记录
    public static final String getOvertimeStaffEnregisterAppURL = BASE_URL+"/app/keepwork/getOvertimeStaffEnregisterApp";//查找对应各部门加班人员登记信息
    public static final String AllKeepWorkStaffAppURL = BASE_URL+"/app/keepwork/AllKeepWorkStaffApp";//获取对应保运值班人员
    public static final String getChangeTaskAppURL = BASE_URL + "/app/eqptRepairDisOrderList/getChangeTaskApp";//获取该条工单的任务类别
    public static final String listAllViEqptRepairTaskMonTaskAppURL = BASE_URL + "/app/eqptRepairDisOrderList/listAllViEqptRepairTaskMonTaskApp";//设备月度任务
    public static final String weatherFillinEqptMonthlyTaskAppURL = BASE_URL + "/app/eqptRepairDisOrderList/weatherFillinEqptMonthlyTaskApp";//判断设备月度任务是否填写
    public static final String autoDetailsAppURL = BASE_URL + "/app/eqptRepairDisOrderList/autoDetailsApp";//添加任务列表
    public static final String getNonChangeTaskAppURL = BASE_URL+"/app/nonEqptRepairDisOrderList/getChangeTaskApp";//获取该条工单下所对应的任务类型
    public static final String nonAutoDetailsAppURL = BASE_URL+"/app/nonEqptRepairDisOrderList/autoDetailsApp";//添加任务列表
    public static final String listAllViNonEqptRepairTaskMonTaskAppURL = BASE_URL+"/app/nonEqptRepairDisOrderList/listAllViNonEqptRepairTaskMonTaskApp";//非设备月度任务
    public static final String listAllNightKeepWorkAppURL = BASE_URL+"/app/keepwork/listAllNightKeepWorkApp";//获取对应保运记录
    public static final String updateNightKeepWorkAndStaffAppURL = BASE_URL+"/app/keepwork/updateNightKeepWorkAndStaffApp";//编辑夜间保运工作记录和夜间保运工作人员
    public static final String addNightKeepWorkAndStaffAppURL = BASE_URL+"/app/keepwork/addNightKeepWorkAndStaffApp";//添加夜间保运工作记录和夜间保运工作人员
    public static final String saveOvertimeStaffEnregisterAppURL = BASE_URL+"/app/keepwork/saveOvertimeStaffEnregisterApp";//保存各部门加班人员登记信息
    public static final String listAllInsDisRouteAppURL = BASE_URL+"/app/ins/listAllInsDisRouteApp";//巡检派工单路线
    public static final String getGroStaffAndUserURL = BASE_URL+"/InsSecDispatch/getGroStaffAndUser";//巡检二次派工获取班组人员和系统用户
    public static final String getDisStaffURL = BASE_URL+"/InsSecDispatch/getDisStaff";//巡检二次派工读取派工人员
    public static final String getSelAnalysisAndMeasURL = BASE_URL+"/InsSecDispatch/getSelAnalysisAndMeas";//巡检二次派工读取安全措施和安全分析
    public static final String sendSecInsOrderAppURL = BASE_URL+"/app/ins/sendSecInsOrderApp";//巡检派工单二次派工
//    public static final String problemscenetreatmentmodeURL = BASE_URL+"/NotEquipmentProblem/ListAllInspproblemscenetreatmentmode";
}
