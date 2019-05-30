package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceImportAgencia;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;

import java.io.File;
import java.util.List;


public class ImportarPanel extends Panel {

    @SpringBean(name = "importarAgenciaService")
    ServiceImportAgencia serviceImportAgencia;

     FileUploadField selectArquivo;

    Form<?> form;


    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");

    public ImportarPanel(String id) {
        super(id);
        form = new Form<Void>("formularioimportar");
        feedbackPanel.setOutputMarkupId(true);
        add(form);
        form.add(feedbackPanel);
        form.add(criarBtnUploadArquivo());
        form.add(criarBtnImportar());
    }

    public FileUploadField criarBtnUploadArquivo() {
        selectArquivo = new FileUploadField("selectArquivo");
        selectArquivo.setOutputMarkupId(true);
        return selectArquivo;
    }

    private AjaxButton criarBtnImportar() {
        AjaxButton importar = new AjaxButton("importar", form) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                final List<FileUpload> uploads = selectArquivo.getFileUploads();
                if (uploads != null) {
                    for (FileUpload upload : uploads) {
                        File newFile = new File("C:\\"
                                + upload.getClientFileName());
                        serviceImportAgencia.lerArqXlsAgencia(newFile);
                    }
                }

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

        };
        importar.setOutputMarkupId(true);
        return importar;
    }
}
