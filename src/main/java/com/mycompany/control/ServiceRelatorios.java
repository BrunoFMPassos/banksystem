package com.mycompany.control;

import com.mycompany.model.*;
import com.mycompany.relatorios.*;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import static org.apache.wicket.ThreadContext.getRequestCycle;

public class ServiceRelatorios<T extends Object> implements Serializable {

    ServiceRelatorios<T> serviceRelatorios;

    public void gererRelatorioPDF(List<T> listaDeObjetos, String nomeRelatorio, String nomeArquivoPdf){
        RelatorioPDF<T> rel = new RelatorioPDF<T>();
        try {
            final byte[] pdfBytes = rel.criarRelatorio(listaDeObjetos,nomeRelatorio);
            if(pdfBytes != null) {
                AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void write(OutputStream output) throws IOException {

                        output.write(pdfBytes);
                        output.close();

                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream);
                handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                handler.setFileName(nomeArquivoPdf+".pdf");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Erro na hora de criar!");
            e.printStackTrace();
        }
    }


    public void gererRelatorioExtratoPDF(List<Movimentacao> listaDeMovimentacoes, Conta conta){
        RelatorioPDFExtrato rel = new RelatorioPDFExtrato();
        try {
            final byte[] pdfBytes = rel.criarRelatorio(listaDeMovimentacoes, conta);
            if(pdfBytes != null) {
                AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void write(OutputStream output) throws IOException {
                        output.write(pdfBytes);
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream);
                handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                handler.setFileName("Extrato.pdf");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Erro na hora de criar!");
            e.printStackTrace();
        }
    }

    public void gererRelatorioTransferenciaPDF(List<Movimentacao> listaDeMovimentacoes, Conta conta){
        RelatorioPDFTransferencia rel = new RelatorioPDFTransferencia();
        try {
            final byte[] pdfBytes = rel.criarRelatorio(listaDeMovimentacoes, conta);
            if(pdfBytes != null) {
                AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void write(OutputStream output) throws IOException {
                        output.write(pdfBytes);
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream);
                handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                handler.setFileName("Comprovante.pdf");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Erro na hora de criar!");
            e.printStackTrace();
        }
    }

    public void gerarRelatorioExcelColaborador(List<Colaborador> listaDeColaboradores){

        RelatorioExcelColaborador excel = new RelatorioExcelColaborador();

        try {
            final byte[] excelBytes = excel.criarExcel(listaDeColaboradores);
            if(excelBytes != null) {
                AbstractResourceStreamWriter  stream = new AbstractResourceStreamWriter() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void write(OutputStream output) throws IOException {
                        output.write(excelBytes);
                        output.close();
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream);
                handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                handler.setFileName("Colaboradores.xls");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Erro na hora de criar!");
            e.printStackTrace();
        }



    }

    public void gerarRelatorioExcelPessoaFisica(List<PessoaFisica> listaDePessoasFisicas){

        RelatorioExcelPessoaFisica excel = new RelatorioExcelPessoaFisica();

        try {
            final byte[] excelBytes = excel.criarExcel(listaDePessoasFisicas);
            if(excelBytes != null) {
                AbstractResourceStreamWriter  stream = new AbstractResourceStreamWriter() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void write(OutputStream output) throws IOException {
                        output.write(excelBytes);
                        output.close();
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream);
                handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                handler.setFileName("Pessoas_Fisicas.xls");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Erro na hora de criar!");
            e.printStackTrace();
        }



    }

    public void gerarRelatorioExcelPessoaJuridica(List<PessoaJuridica> listaDePessoasJuridicas){

        RelatorioExcelPessoaJuridica excel = new RelatorioExcelPessoaJuridica();

        try {
            final byte[] excelBytes = excel.criarExcel(listaDePessoasJuridicas);
            if(excelBytes != null) {
                AbstractResourceStreamWriter  stream = new AbstractResourceStreamWriter() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void write(OutputStream output) throws IOException {
                        output.write(excelBytes);
                        output.close();
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(stream);
                handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                handler.setFileName("Pessoas_Juridicas.xls");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Erro na hora de criar!");
            e.printStackTrace();
        }



    }
}
