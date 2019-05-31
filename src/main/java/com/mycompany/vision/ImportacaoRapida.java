package com.mycompany.vision;
import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.control.ServiceAgencia;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import java.io.File;
import java.io.IOException;
import java.util.List;



public class ImportacaoRapida extends BasePage{

    @SpringBean(name = "agenciaService")
    ServiceAgencia serviceAgencia;

    private static final String TARGET_PATH = "C:\\temp";
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
    FeedbackPanel feedbackPanelSuccess = new FeedbackPanel("feedbacksuccess");


    public ImportacaoRapida() {
        add(new UploadForm("uploadForm"));
    }

    private class UploadForm extends Form<Void> {

        private List<FileUpload> uploads;

        public UploadForm(String id) {
            super(id);
            FileUploadField uploadField = new FileUploadField("uploadField",
                    new PropertyModel<List<FileUpload>>(this, "uploads"));
            feedbackPanel.setOutputMarkupId(true);
            feedbackPanelSuccess.setOutputMarkupId(true);
            add(uploadField);
            add(criarBtnImportar());
            add(feedbackPanel);
            add(feedbackPanelSuccess);
        }


        public AjaxButton criarBtnImportar(){
            AjaxButton importar = new AjaxButton("importar"){
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    super.onSubmit(target, form);
                    try {
                        String filePath = TARGET_PATH + "\\"
                                + uploads.get(0).getClientFileName();
                        uploads.get(0).writeTo(new File(filePath));
                        File file = new File(filePath);
                        serviceAgencia.executarAoClicarNoImportar(file,feedbackPanel,feedbackPanelSuccess,target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            return importar;
        }

        @Override
        protected void onError() {
            super.onError();
        }
    }
}
