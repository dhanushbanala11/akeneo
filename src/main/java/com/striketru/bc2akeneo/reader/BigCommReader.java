package com.striketru.bc2akeneo.reader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.striketru.bc2akeneo.transformer.BC2PIMTransformer;
import com.striketru.bc2akeneo.writer.PIMWriter;
import com.striketru.bc2akeneo.writer.WriterData;
import com.striketru.conn.base.Reader;

public class BigCommReader extends Reader {
	public static final Logger LOGGER = LogManager.getLogger(BigCommReader.class);

	public static BigCommAPI bigCommAPI = null;
	public static BC2PIMTransformer bc2pimTranformer = new BC2PIMTransformer();
	public static PIMWriter pimWriter;
	public static final int LOAD_VISIBLE_COUNT = 50000;
	public static int currentVisibleCount = 0;

	public enum RUN_TYPE {INV_TEST, CUSTOMER_SPECIFIC, PAGE, JSON, PAGELIMIT}
	
	public BigCommReader(Map<String, String> appProp ){
		String url = appProp.get("bigComm_url");
		url = url.replace("$$bigComm_storeHash##", appProp.get("bigComm_storeHash"));
		LOGGER.info(url);
		bigCommAPI = new BigCommAPI(appProp.get("bigComm_authClient"), appProp.get("bigComm_authToken"), appProp.get("bigComm_storeHash"), url);
		pimWriter = new PIMWriter(appProp);
	}

	@Override
	public void execute() {
		try {
			boolean isLoadImage = false;
			boolean isLoadDocs = false;
			RUN_TYPE currentRun = RUN_TYPE.CUSTOMER_SPECIFIC;
			
			if (currentRun == RUN_TYPE.INV_TEST) {
//				Set<String> customerList = new HashSet<>(Arrays.asList("4812","4814","4876","4877","4879","4880","4881","4884","4885","4886","4887","4888","4889","4890","4891","4892","4893","4894","4895","4896","5092","5097","5126","5128","5129","5130","5131","5141","5145","5146","5150","5156","5202","5203","5204","5205","6069","6310","6311","6312","6444","6613","6615","6616","6909","6911","6978","7115","7295","7680","8027","8345","8368","2023","9250"));
				Set<String> customerList = new HashSet<>(Arrays.asList("2832"));
				List<Object> dataTemp =  bigCommAPI.getData(customerList); //"9147", "2864", "440", "375", "233", "7834","7079","7790",
				executeProductPage(dataTemp, isLoadImage, isLoadDocs);
			} else if (currentRun == RUN_TYPE.CUSTOMER_SPECIFIC) {
				Set<String> customerList = getCustomerListOfIds();
				Set<String> ourList = getOurListOfIds();
				ourList.addAll(customerList);
				List<Object> dataTemp =  bigCommAPI.getData(ourList); //"9147", "2864", "440", "375", "233" 
				executeProductPage(dataTemp, isLoadImage, isLoadDocs);
			} else if(currentRun == RUN_TYPE.PAGE) {
				int pageCount = bigCommAPI.getDataPageCount();
				//pageCount = 50; // temp statement
				List<Object> data = null;
				for (int i= 1; i <= pageCount; i++) {
					data =  bigCommAPI.getDataByPage(i);
					boolean isContinue = executeProductPage(data, isLoadImage, isLoadDocs);
					if (!isContinue) {
						break;
					}
					
				}
			}else if(currentRun == RUN_TYPE.JSON) {
				int pageCount = bigCommAPI.getDataPageCount();
				List<Object> data = null;
				for (int i= 1; i <= pageCount; i++) {
					data =  bigCommAPI.getDataByPage(i);
					boolean isContinue = getProductPageJson(data);
					if (!isContinue) {
						break;
					}
				}
			}else {
				int pageCount = bigCommAPI.getDataPageCount();
				int StartPageCount = 20;
				int EndPageCount = 22;
				List<Object> data = null;
				for (int i= StartPageCount; i <= EndPageCount; i++) {
					data =  bigCommAPI.getDataByPage(i);
					boolean isContinue = executeProductPage(data, isLoadImage, isLoadDocs);
					if (!isContinue) {
						break;
					}
					
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean executeProductPage(List<Object> data, boolean isLoadImage, boolean isLoadDocs){
		for (Object productData : data) {
			ReaderData reader = new ReaderData(productData);
			if (isLoadNextData(reader)) {
				if (isPermanentHidden(reader)) { 
					WriterData writerData = bc2pimTranformer.execute(reader);
					pimWriter.execute(writerData);
					pimWriter.executeMediaFiles(writerData, isLoadImage, isLoadDocs);
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	public boolean getProductPageJson(List<Object> data){
		for (Object productData : data) {
			ReaderData reader = new ReaderData(productData);
			if (isPermanentHidden(reader)) {
				pimWriter.writeJsonToFile(productData, reader.getBcId(), false);
			}else {
				pimWriter.writeJsonToFile(productData, reader.getBcId(), true);
			}
		}
		return true;
	}

	private boolean isPermanentHidden(ReaderData reader) {
		if (reader.getProduct().get("categories") != null ) { 
			Set<Integer> categories = new HashSet((List<Object>)reader.getProduct().get("categories"));
			return !categories.contains(769);
		}
		return false;
	}
	
	private boolean isLoadNextData(ReaderData reader) {
		if (currentVisibleCount < LOAD_VISIBLE_COUNT) {
			if (reader.getProduct().get("is_visible") != null && reader.getProduct().get("is_visible").toString().equalsIgnoreCase("true")  ) {
				currentVisibleCount++;
			}
			return true;
		}
		return false;
	}

	
	private Set<String> getOurListOfIds(){
		return new HashSet<>(Arrays.asList());
		//"1000","1001","1002","1003","1004","1005","1006","1007","1008","1009","1010","1011","1012","1013","1014","1015","1016","1017","1018","1019","1020","1021","1022","1023","1024","1025","1026","1027","1028","1029","1030","1031","1032","1034","1035","1036","1037","1038","1039","1041","1042","1043","1044","1045","1046","1048","1049","1051","1052","1053","1054","1055","1057","1064","1065","1066","1067","1068","1070","1071","1073","1074","1075","1076","1077","1078","1079","1080","1081","1082","1083","1084","1085","1086","1087","1088","1089","1090","1091","1092","1093","1094","1095","1096","1097","1098","1099","1100","1101","1102","1103","1104","1105","1106","1107","1108","1109","1110","1111","1112","1113","1114","1115","1116","1117","1118","1119","1120","1121","1122","227","228","229","230","231","232","233","234","235","236","237","238","239","240","241","242","243","244","245","246","247","248","249","250","251","252","254","255","256","257","258","259","260","261","262","263","264","265","266","267","268","269","270","271","272","273","274","275","276","277","278","279","280","281","282","283","284","285","286","287","288","289","290","291","292","293","294","295","296","297","298","299","300","301","302","303","304","305","306","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","323","324","325","327","328","330","331","332","333","334","335","336","337","338","339","340","341","342","343","344","345","346","347","348","349","350","351","352","353","354","355","356","357","358","359","360","361","362","363","364","365","366","367","368","369","370","371","372","373","374","375","376","377","378","379","380","381","382","383","384","385","386","387","388","389","390","391","392","393","394","395","396","397","398","399","400","401","402","403","404","405","406","407","408","409","410","411","412","413","414","418","420","421","422","423","424","425","426","427","428","429","430","431","434","435","436","437","438","439","440","441","442","443","444","445","446","447","448","449","450","451","452","453","454","455","456","457","458","459","460","461","462","463","465","466","467","468","469","470","472","473","474","475","476","477","482","483","484","485","486","487","490","491","492","493","494","495","496","497","498","499","500","501","502","503","504","505","506","507","508","509","510","511","512","513","514","515","516","517","518","519","520","521","522","523","524","525","532","533","538","539","540","541","542","543","544","545","546","547","548","549","550","551","553","554","555","556","557","558","560","561","563","564","566","567","568","569","570","571","572","573","574","575","576","577","578","579","580","581","583","584","585","586","587","588","589","590","591","592","593","594","595","596","597","598","599","601","603","604","605","606","607","608","609","610","611","612","613","614","615","616","617","618","619","620","621","622","623","624","625","626","627","628","629","630","631","632","633","634","635","636","637","638","639","640","641","642","643","644","645","646","647","648","649","650","651","652","653","654","655","656","657","658","659","660","661","662","663","664","665","666","667","668","669","670","671","672","673","674","675","676","677","678","679","680","681","682","683","684","685","686","687","688","689","690","691","692","693","694","695","696","697","698","699","700","701","702","703","704","705","706","707","708","709","710","711","712","713","714","715","716","717","718","719","720","721","722","723","724","725","726","727","728","729","730","731","732","733","734","735","736","737","738","739","740","741","742","743","744","745","746","747","748","749","750","751","752","753","754","755","756","757","758","759","760","761","762","763","764","765","766","767","768","769","770","771","772","773","774","775","776","777","778","779","780","781","782","783","784","785","786","787","788","789","790","791","792","793","794","795","796","797","798","799","800","801","802","803","804","805","806","807","808","809","810","811","812","813","814","815","816","817","818","819","820","821","822","823","824","825","826","827","828","829","830","831","832","833","834","835","836","837","838","839","840","841","842","843","844","845","846","847","848","849","850","851","852","853","854","855","856","857","858","859","860","861","862","863","864","865","866","867","868","869","870","871","872","873","874","875","876","877","878","879","880","881","882","883","884","885","886","887","888","889","890","891","892","893","894","895","896","897","898","899","900","901","902","903","904","905","906","907","908","909","910","911","912","913","914","915","916","917","918","919","920","921","922","923","924","925","926","927","928","929","930","931","932","933","934","935","936","937","938","939","940","941","942","943","944","945","946","947","948","949","950","951","952","953","954","955","956","957","958","959","960","961","962","963","964","965","966","967","968","969","970","971","972","973","974","975","976","977","978","979","980","981","982","983","984","985","986","987","988","989","990","991","992","993","994","995","996","997","998","999"
	}

	private Set<String> getCustomerListOfIds(){
		return new HashSet<>(Arrays.asList("2832")); // 9236, 5150, 8389, 1380, 5533, 8009, 3221, 6993, "5134","613","6993"
//		return new HashSet<>(Arrays.asList("8009","3620","473","1007","1045","1380","1403","1548","1905","2486","3221","3319","3405","3551","3598","3672","4002","4245","4199","4352","4517","4839","4814","4865","4991","5215","5533","5671","5744","5915","6162","6424","6453","6476","6485","6557","6549","6993","7178","7239","7275","7592","7940","8054","8522","8739","1089","1942","338","6508","8389","8390","8612","8602","8580","9236","9247","6980","5134","5150","6362","613","6318","7229","5215","9243"));
		//return new HashSet<>(Arrays.asList("8580","8581","8585","8602","8606","8607","8608","8609","8617","8618","8619","8620","8621","8622","8623","8624","8625","9092","9120","9131","9155","9228"));
		//"8009","3620","473","1007","1045","1380","1403","1548","1905","2486","3221","3319","3405","3551","3598","3672","4002","4245","4199","4352","4517","4839","4814","4865","4991","5215","5533","5671","5744","5915","6162","6424","6453","6476","6485","6557","6549","6993","7178","7239","7275","7592","7940","8054","8522","8739","1089","1942","338","6508"
	}


}
