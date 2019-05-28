package com.mycompany.relatorios;

import com.mycompany.model.Conta;
import com.mycompany.model.Movimentacao;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioPDFTransferencia {
    public static void main(String[] args){


    }

    public byte[] criarRelatorio(List<Movimentacao> lista, Conta conta) throws Exception{

        Map parameters = new HashMap();
        parameters.put("numeroConta", conta.getNumero().toString());
        parameters.put("digitoConta", conta.getDigito().toString());
        parameters.put("agenciaConta", conta.getAgencia().getNumero());
        parameters.put("moeda","R$");


        JRDataSource data = new JRBeanCollectionDataSource(lista);
        InputStream rellnStream;
        rellnStream = RelatorioPDF.class.getClassLoader().getResource("relatorios/Transferencia.jasper").openStream();
        return JasperRunManager.runReportToPdf(rellnStream, parameters, data);

    }
}
