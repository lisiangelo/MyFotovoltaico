package it.android.j940549.fotovoltaico_material_design.Model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by J940549 on 18/05/2017.
 */

public class ConvertiData {
    long datainmill;
    GregorianCalendar data;
    String datas="";
    String strBimestre="";
    String strPeriodo="";
    int intbimestre=0;
    int intperiodo=0;
    public ConvertiData(){
        datainmill=0;
        data= new GregorianCalendar();
    }
    public String long_data(long mill){
        data.setTimeInMillis(mill*1000);
        datas=""+data.get(Calendar.DAY_OF_MONTH)+"/"+(data.get(Calendar.MONTH)+1)+"/"+data.get(Calendar.YEAR);
        return datas;
    }
    public long data_longtoString(String dt){
        String[]dataarray=dt.split("/");
        String gg = dataarray[0];
        String mm = ""+ (Integer.parseInt(dataarray[1])-1);
        String aa = dataarray[2];
        int  anno = Integer.parseInt(aa);
        int mese = Integer.parseInt(mm);
        int giorno = Integer.parseInt(gg);
        data= new GregorianCalendar (anno,mese,giorno);
        datainmill=data.getTimeInMillis()/1000;
        return datainmill;
    }
    public long data_long( String gg, String mm, String aa){

        switch(mm){
            case "Gen": mm="0";break;
            case "Feb": mm="01";break;
            case "Mar": mm="02";break;
            case "Apr": mm="03";break;
            case "Mag": mm="04";break;
            case "Giu": mm="05";break;
            case "Lug": mm="06";break;
            case "Ago": mm="07";break;
            case "Set": mm="08";break;
            case "Ott": mm="09";break;
            case "Nov": mm="10";break;
            case "Dic": mm="11";break;
        }
        int  anno = Integer.parseInt(aa);
        int mese = Integer.parseInt(mm);
        int giorno = Integer.parseInt(gg);
        data= new GregorianCalendar (anno,mese,giorno);
        datainmill=data.getTimeInMillis()/1000;
        return datainmill;
    }
    public int intBimestre (String bim){

        switch(bim){
            case "Gen-Feb": intbimestre=1;break;
            case "Mar-Apr": intbimestre=2;break;
            case "Mag-Giu": intbimestre=3;break;
            case "Lug-Ago": intbimestre=4;break;
            case "Set-Ott": intbimestre=5;break;
            case "Nov-Dic": intbimestre=6;break;
        }
        return intbimestre;
    }
    public int intPeriodo (String per){

        switch(per){
            case "I° Acconto": intperiodo=1;break;
            case "II° Acconto": intperiodo=2;break;
            case "III° Acconto": intperiodo=3;break;
            case "IV° Acconto": intperiodo=4;break;
            case "Conguaglio": intperiodo=5;break;

        }
        return intperiodo;
    }
    public String strBimestre (int bim){

        switch(bim){
            case 1: strBimestre="Gen-Feb";break;
            case 2: strBimestre="Mar-Apr";break;
            case 3: strBimestre="Mag-Giu";break;
            case 4: strBimestre="Lug-Ago";break;
            case 5: strBimestre="Set-Ott";break;
            case 6: strBimestre="Nov-Dic";break;
        }
        return strBimestre;
    }
    public String strPeriodo (int per){

        switch(per){
            case 1: strPeriodo="I° Acconto";break;
            case 2: strPeriodo="II° Acconto";break;
            case 3: strPeriodo="III° Acconto";break;
            case 4: strPeriodo="IV° Acconto";break;
            case 5: strPeriodo="Conguaglio";break;

        }
        return strPeriodo;
    }

public String getMese_int_to_Str(int mm){
    String mese="";
    switch(mm){
        case 0: mese="Gen";break;
        case 1: mese="Feb";break;
        case 2: mese="Mar";break;
        case 3: mese="Apr";break;
        case 4: mese="Mag";break;
        case 5: mese="Giu";break;
        case 6: mese="Lug";break;
        case 7: mese= "Ago";break;
        case 8: mese="Set";break;
        case 9: mese="Ott";break;
        case 10: mese="Nov";break;
        case 11: mese="Dic";break;
    }
    return mese;
}
}
