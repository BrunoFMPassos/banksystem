package com.mycompany.control;

import com.mycompany.DAO.DaoColaborador;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Agencia;
import com.mycompany.model.Colaborador;
import com.mycompany.model.User;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class ServiceColaborador {

    @SpringBean(name = "colaboradorDao")
    private DaoColaborador colaboradorDao;
    @SpringBean(name = "genericDao")
    private GenericDao<Colaborador> genericDao;
    @SpringBean(name = "agenciaService")
    private ServiceAgencia agenciaService;

    public Mensagem inserir(Colaborador colaborador) {

        Mensagem mensagem = new Mensagem();
        Boolean colaboradorNull = verificaSeColaboradorNullParaInserir(colaborador);
        if (colaborador.getPassword() != null) {
            if (colaboradorNull) {
                if (verificaSeUsuarioUnicoParaInserir(colaborador)) {
                    if (verificaSeCPFUnicoParaInserir(colaborador)) {
                        User user = pesquisarObjetoUserPorColaborador(colaborador);
                        Boolean userNull = verificaSeUserNull(user);
                        if (userNull) {
                            user = new User();
                            colaborador.setUser(user);
                        }
                        preparaUserParaInserir(colaborador, user);
                        colaboradorDao.inserir(colaborador, user);
                    } else {
                        mensagem.adcionarMensagemNaLista("Cpf já existente!");
                    }
                } else {
                    mensagem.adcionarMensagemNaLista("Username já existente!");
                }
            } else {
                mensagem.adcionarMensagemNaLista("Colaborador já existente!");
            }
        }else{
            mensagem.adcionarMensagemNaLista("O campo senha é obrigatório!");
        }
        return mensagem;
    }

    public Mensagem update(Colaborador colaborador) {
        Mensagem mensagem = new Mensagem();
        Boolean colaboradorNull = verificaSeColaboradorNullParaUpdate(colaborador);
        if (!colaboradorNull) {
            if (verificaSeUsuarioUnicoParaUpdate(colaborador)) {
                if (verificaSeCPFUnicoParaUpdate(colaborador)) {
                    User user = pesquisarObjetoUserPorColaborador(colaborador);
                    preparaUserParaInserir(colaborador, user);
                    colaboradorDao.inserir(colaborador, user);
                } else {
                    mensagem.adcionarMensagemNaLista("Cpf já existente!");
                }
            } else {
                mensagem.adcionarMensagemNaLista("Usuário já existente!");
            }
        } else {
            mensagem.adcionarMensagemNaLista("Colaborador já existente!");
        }
        return mensagem;
    }

    public Colaborador pesquisarObjetoColaboradorPorNome(String nome) {
        return colaboradorDao.pesquisaObjetoColaboradorPorNome(nome);
    }

    public Colaborador pesquisarObjetoColaboradorPorId(Long id) {
        return colaboradorDao.pesquisaObjetoColaboradorPorId(id);
    }

    public User pesquisarObjetoUserPorColaborador(Colaborador colaborador) {
        return colaboradorDao.pesquisarObjetoUserPorColaborador(colaborador);
    }

    public List<Colaborador> pesquisarListaDeColaboradoresPorColabordaor(Colaborador colaborador) {
        List<Colaborador> listaDeColaboradores = genericDao.pesquisarListaDeObjeto(colaborador);
        trazerDadosDoUserParaOColaborador(listaDeColaboradores);
        return listaDeColaboradores;
    }

    public void trazerDadosDoUserParaOColaborador(List<Colaborador> listaDeColaboradores) {
        for (Colaborador colaborador : listaDeColaboradores) {
            colaborador.setUsername(colaborador.getUser().getUsername());
            colaborador.setPerfil(colaborador.getUser().getPerfil());
        }
    }

    public List<Colaborador> pesquisarListaDeColaboradoresPorNome(Colaborador colaborador, String colum, String string) {
        return genericDao.pesquisaListadeObjetosPorString(colaborador, colum, string);
    }


    public List<Colaborador> pesquisaListaDeColaboradorPorAgencia(Colaborador colaborador, Agencia agencia) {
        return colaboradorDao.pesquisaListadeObjetoColaboradorPorAgencia(colaborador, agencia);
    }

    public void deletarColaborador(Colaborador colaborador) {
        colaboradorDao.deletar(colaborador);
    }

    public void filtrarColaboradorNaVisao(String nome, String agencia, List<Colaborador> listaDeColaboradores, Colaborador colaborador, AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!nome.isEmpty() && agencia.isEmpty()) {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorNome(colaborador, "nome", nome));
            target.add(rowPanel);

        } else if (nome.isEmpty() && !agencia.isEmpty()) {
            listaDeColaboradores.clear();
            Agencia agenciaObj = agenciaService.pesquisaObjetoAgenciaPorNumero(agencia);
            listaDeColaboradores.addAll(pesquisaListaDeColaboradorPorAgencia(colaborador, agenciaObj));
            target.add(rowPanel);

        } else if (!nome.isEmpty() && !agencia.isEmpty()) {
            listaDeColaboradores.clear();
            List<Colaborador> listaAuxiliarColaboradoresPorAgencias = new ArrayList<Colaborador>();
            List<Colaborador> listaAuxiliarColaboradoresPorNomes = new ArrayList<Colaborador>();
            Agencia agenciaObj = agenciaService.pesquisaObjetoAgenciaPorNumero(agencia);
            listaAuxiliarColaboradoresPorAgencias.addAll(pesquisaListaDeColaboradorPorAgencia(colaborador, agenciaObj));
            listaAuxiliarColaboradoresPorNomes.addAll(pesquisarListaDeColaboradoresPorNome(colaborador, "nome", nome));

            for (Colaborador colaboradorloop : listaAuxiliarColaboradoresPorAgencias) {
                Colaborador colaboradorAuxiliar = colaboradorloop;
                for (Colaborador colaboradorloop2 : listaAuxiliarColaboradoresPorNomes) {
                    if (colaboradorAuxiliar.getNome().equals(colaboradorloop2.getNome())) {
                        listaDeColaboradores.add(colaboradorloop);
                    }
                }
            }

            target.add(rowPanel);
        } else {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorColabordaor(colaborador));
        }
        target.add(rowPanel);
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<Colaborador> listaDeColaboradores, Colaborador colaborador,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(colaborador);
        if (mensagem.getListaVazia()) {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorColabordaor(colaborador));
            modalWindow.close(target);
            target.add(rowPanel);
        } else {
            int index = 0;
            for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }

    public void executarAoClicarEmSalvarNaModalEditar(
            List<Colaborador> listaDeColaboradores, Colaborador colaborador,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Colaborador colaboradorExistente = pesquisarObjetoColaboradorPorId(colaborador.getId());
        if (colaborador.getPassword() == null) {
            colaborador.setPassword(colaboradorExistente.getUser().getPassword());
        }
        Mensagem mensagem = update(colaborador);

        if (mensagem.getListaVazia()) {
            listaDeColaboradores.clear();
            listaDeColaboradores.addAll(pesquisarListaDeColaboradoresPorColabordaor(colaborador));
            modalWindow.close(target);
            target.add(rowPanel);
        } else {
            int index = 0;
            for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }

    public boolean verificaSeColaboradorNullParaInserir(Colaborador colaborador) {
        Boolean colaboradorNull = true;
        Colaborador colaboradorparaVerificar = colaboradorDao.pesquisaObjetoColaboradorPorNome(colaborador.getNome());
        if (colaboradorparaVerificar == null) {
            colaboradorNull = true;
        } else {
            colaboradorNull = false;
        }
        return colaboradorNull;
    }


    public boolean verificaSeColaboradorNullParaUpdate(Colaborador colaborador) {
        Boolean colaboradorNull = true;
        Colaborador colaboradorparaVerificar = colaboradorDao.pesquisaObjetoColaboradorPorId(colaborador.getId());
        if (colaboradorparaVerificar == null) {
            colaboradorNull = true;
        } else {
            colaboradorNull = false;
        }
        return colaboradorNull;
    }

    public boolean verificaSeUsuarioUnicoParaInserir(Colaborador colaborador) {
        Boolean usuarioUnico = true;
        User user = new User();
        int verificador = 0;
        for (User usuarioDaLista : colaboradorDao.pesquisarListaDeUsuariosExistentes()) {
            if (usuarioDaLista.getUsername().equals(colaborador.getUsername())) {
                verificador++;
            }
        }
        if (verificador > 0) {
            usuarioUnico = false;
        }
        return usuarioUnico;
    }

    public boolean verificaSeUsuarioUnicoParaUpdate(Colaborador colaborador) {
        Boolean usuarioUnico = true;
        User user = new User();
        int verificador = 0;
        for (User usuarioDaLista : colaboradorDao.pesquisarListaDeUsuariosExistentes()) {
            if (usuarioDaLista.getUsername().equals(colaborador.getUsername()) && usuarioDaLista.getId() != colaborador.getUser().getId()) {
                verificador++;
            }
        }
        if (verificador > 0) {
            usuarioUnico = false;
        }
        return usuarioUnico;
    }


    public void preparaUserParaInserir(Colaborador colaborador, User user) {
        user.setUsername(colaborador.getUsername());
        user.setPassword(colaborador.getPassword());
        user.setPerfil(colaborador.getPerfil());
    }

    public boolean verificaSeUserNull(User user) {
        Boolean userNull;
        if (user == null) {
            userNull = true;
        } else {
            userNull = false;
        }
        return userNull;
    }

    public boolean verificaSeCPFUnicoParaInserir(Colaborador colaborador) {
        Boolean cpfUnico = true;
        int verificador = 0;
        for (Colaborador colaboradorDaLista : colaboradorDao.pesquisarListaDeColaboradoresExistentes()) {
            if (colaboradorDaLista.getCpf().equals(colaborador.getCpf())) {
                verificador++;
            }
        }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }

    public boolean verificaSeCPFUnicoParaUpdate(Colaborador colaborador) {
        Boolean cpfUnico = true;
        int verificador = 0;
        for (Colaborador colaboradorDaLista : colaboradorDao.pesquisarListaDeColaboradoresExistentes()) {
            System.out.println(colaboradorDaLista.getId());
            if (!colaborador.getId().toString().equals(colaboradorDaLista.getId().toString())) {
                if (colaboradorDaLista.getCpf().equals(colaborador.getCpf())) {
                    verificador++;
                }
            }
        }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }


    public void setColaboradorDao(DaoColaborador colaboradorDao) {
        this.colaboradorDao = colaboradorDao;
    }

    public void setGenericDao(GenericDao<Colaborador> genericDao) {
        this.genericDao = genericDao;
    }

    public void setAgenciaService(ServiceAgencia agenciaService) {
        this.agenciaService = agenciaService;
    }
}
