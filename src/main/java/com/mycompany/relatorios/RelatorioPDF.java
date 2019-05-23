package com.mycompany.relatorios;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioPDF<T extends  Object> implements Serializable {

    public static void main(String[] args){


    }

    public byte[] criarRelatorio(List<T> lista, String nomeRelatorio) throws Exception{

        JRDataSource data = new JRBeanCollectionDataSource(lista);
        InputStream rellnStream;
        rellnStream = RelatorioPDF.class.getClassLoader().getResource("relatorios/"+nomeRelatorio+".jasper").openStream();
        return JasperRunManager.runReportToPdf(rellnStream, null, data);

    }
}
