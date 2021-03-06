package dao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import model.Paciente;
import java.text.SimpleDateFormat;
import model.Sessoes;

/**
 *
 * @author Rodrigo
 */
public class PacienteDAO {
    private Connection connection;
    
    public PacienteDAO() {
        ConnectionClass con = new ConnectionClass();
        try {
            connection = con.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addPaciente(Paciente paciente) {        
        Integer id = InserirPaciente(paciente);//grava apenas as informações na tabela pacientes 
        if (id != null){
            paciente.setIdpacientes(id);
        }
        int qtd_sessoes = paciente.getQtd_sessoes();
        int segunda = 0;
        int quarta = 0;
        int sexta = 0;
        boolean bsegunda = false;
        boolean bquarta = false;
        boolean bsexta = false;
        int qtd_diaSemana = 0;
        
        if (paciente.getSegunda().equals("1")){
            bsegunda = true;
            qtd_diaSemana ++;
        }
        if (paciente.getQuarta().equals("1")){
            bquarta = true;
            qtd_diaSemana ++;
        }
        if (paciente.getSexta().equals("1")){
            bsexta = true;
            qtd_diaSemana ++;
        }
        
        double divDias = ((double)qtd_sessoes/(double)qtd_diaSemana); 
        if (qtd_diaSemana==1){
            if(bsegunda){
                segunda = qtd_sessoes;
                InserirLoop(paciente, Calendar.MONDAY, segunda);
            }else if(bquarta){
                quarta = qtd_sessoes;
                InserirLoop(paciente, Calendar.WEDNESDAY, quarta);
            }else{
                sexta = qtd_sessoes;
                InserirLoop(paciente, Calendar.FRIDAY, sexta);
            }   
        }else if(qtd_diaSemana==2){
            if(bsegunda && bquarta){
                segunda = (int)divDias;
                quarta = (int)Math.ceil(divDias);
                InserirLoop(paciente, Calendar.MONDAY, segunda);
                InserirLoop(paciente, Calendar.WEDNESDAY, quarta);
            }
            if(bsegunda && bsexta){
                segunda = (int)divDias;
                sexta = (int)Math.ceil(divDias);
                InserirLoop(paciente, Calendar.MONDAY, segunda);
                InserirLoop(paciente, Calendar.FRIDAY, sexta);
            }
            if(bquarta && bsexta){
                quarta = (int)divDias;
                sexta = (int)Math.ceil(divDias);
                InserirLoop(paciente, Calendar.WEDNESDAY, quarta);
                InserirLoop(paciente, Calendar.FRIDAY, sexta);
            }
        }else if(qtd_diaSemana==3){
            segunda = (int)divDias;
            quarta = (int)divDias;
            //sexta = (int)Math.ceil(divDias);
            sexta = qtd_sessoes - (segunda + quarta);
                if((sexta - segunda) == 2){
                    segunda ++;
                    sexta = segunda;
                }
            InserirLoop(paciente, Calendar.MONDAY, segunda);
            InserirLoop(paciente, Calendar.WEDNESDAY, quarta);
            InserirLoop(paciente, Calendar.FRIDAY, sexta);
        }
        
    }
    
    public void InserirLoop(Paciente paciente, int diaSemana, int quantidade){
                Calendar date1 = Calendar.getInstance();
                date1.add(Calendar.DATE, 1);
                String dias = "segunda-feira";
                if (diaSemana==Calendar.WEDNESDAY){
                    dias = "quarta-feira";
                }else if(diaSemana==Calendar.FRIDAY){
                    dias = "sexta-feira";
                }
                while (date1.get(Calendar.DAY_OF_WEEK) != diaSemana){
                    date1.add(Calendar.DATE, 1);
                }
                
                for(int x=0; x < quantidade; x++){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    paciente.setData(sdf.format(date1.getTime()));
                    InserirPacientesSessoes(paciente, dias);
                    date1.add(Calendar.DATE, 7);
                }
    }
    
    public Integer InserirPaciente(Paciente paciente){
        //int id = 0;
        try {          
            String query = "INSERT INTO pacientes"
                    +"("
                    + "num_sus, "
                    + "nome, "
                    + "telefone, "
                    + "dt_nasc, "
                    + "idade, "
                    + "sexo, "
                    + "rg, "
                    + "dt_emissao, "
                    + "org_emissor, "
                    + "nome_pai, " 
                    + "nome_mae, "
                    + "profissao, "
                    + "raca_cor, "
                    + "cep, "
                    + "endereco, "
                    + "numero_casa, "
                    + "bairro, "
                    + "uf, "
                    + "cidade, "
                    + "diag_clinico, "
                    + "diag_fiso, "
                    + "anamnese, "
                    + "hma, "
                    + "hmp, "
                    + "ant_hereditario, "
                    + "alg_cirurgia, "
                    + "qual_Cirurgia, "
                    + "tabagista, "
                    + "num_cigarros, "
                    + "etilista, "
                    + "qtd_etilista, "
                    + "sedentario, "
                    + "freq_sendentario, "
                    + "medicamentos, "
                    + "quais_medicamentos, "
                    + "inicio_sintoma, "
                    + "mecanismo_sintoma, "
                    + "acomp_sintoma, "
                    + "qual_sintoma, "
                    + "localizacao_dor, "
                    + "carater_dor, "
                    + "irradiacao_dor, "
                    + "local_dor, "
                    + "movimento_dor, "
                    + "qual_dor, "
                    + "repouso_dor, "
                    + "climatica_dor, "
                    + "esforco_dor, "
                    + "qual_esforco, "
                    + "escala_eva, "
                    + "inspecao_exame, "
                    + "tonus_exame, "
                    + "qtd_sessoes, "
                    + "carac_exame) "
                    /*+ "qtd_sessoes, "
                    + "dias_sessoes, "
                    + "hora_sessoes, "
                    + "data) values "*/
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS); // retorna o id do paciente
            stmt.setInt(1, paciente.getNum_sus()); 
            stmt.setString(2, paciente.getNome()); 
            stmt.setString(3, paciente.getTelefone()); 
            stmt.setString(4, paciente.getDt_nasc()); 
            stmt.setInt(5, paciente.getIdade()); 
            stmt.setString(6, paciente.getSexo()); 
            stmt.setInt(7, paciente.getRg());
            stmt.setString(8, paciente.getDt_emissao());
            stmt.setString(9, paciente.getOrg_emissor()); 
            stmt.setString(10, paciente.getNome_pai()); 
            stmt.setString(11, paciente.getNome_mae()); 
            stmt.setString(12, paciente.getProfissao()); 
            stmt.setString(13, paciente.getRaca_cor()); 
            stmt.setString(14, paciente.getCep());
            stmt.setString(15, paciente.getEndereco());
            stmt.setInt(16, paciente.getNumero_casa());
            stmt.setString(17, paciente.getBairro());
            stmt.setString(18, paciente.getUf());
            stmt.setString(19, paciente.getCidade());
            stmt.setString(20, paciente.getDiag_clinico());
            stmt.setString(21, paciente.getDiag_fiso());
            stmt.setString(22, paciente.getAnamnese());
            stmt.setString(23, paciente.getHma());
            stmt.setString(24, paciente.getHmp());
            stmt.setString(25, paciente.getAnt_hereditario());
            stmt.setInt(26, paciente.getAlg_cirurgia());
            stmt.setString(27, paciente.getQual_cirurgia());
            stmt.setInt(28, paciente.getTabagista());
            stmt.setString(29, paciente.getNum_cigarros());
            stmt.setInt(30, paciente.getEtilista());
            stmt.setString(31, paciente.getQtd_etilista());
            stmt.setInt(32, paciente.getSedentario());
            stmt.setString(33, paciente.getFreq_sendentario());
            stmt.setInt(34, paciente.getMedicamentos());
            stmt.setString(35, paciente.getQuais_medicamentos());
            stmt.setString(36, paciente.getInicio_sintoma());
            stmt.setString(37, paciente.getMecanismo_sintoma());
            stmt.setInt(38, paciente.getAcomp_sintoma());
            stmt.setString(39, paciente.getQual_sintoma());
            stmt.setString(40, paciente.getLocalizacao_dor());
            stmt.setString(41, paciente.getCarater_dor());
            stmt.setInt(42, paciente.getIrradiacao_dor());
            stmt.setString(43, paciente.getLocal_dor());
            stmt.setInt(44, paciente.getMovimento_dor());
            stmt.setString(45, paciente.getQual_dor());
            stmt.setInt(46, paciente.getRepouso_dor());
            stmt.setInt(47, paciente.getClimatica_dor());
            stmt.setInt(48, paciente.getEsforco_dor());
            stmt.setString(49, paciente.getQual_esforco());
            stmt.setInt(50, paciente.getEscala_eva());
            stmt.setString(51, paciente.getInspecao_exame());
            stmt.setString(52, paciente.getTonus_exame());
            stmt.setInt(53, paciente.getQtd_sessoes());
            stmt.setString(54, paciente.getCarac_exame());
            /*stmt.setInt(54, paciente.getQtd_sessoes());
            stmt.setString(55, dias_sessoes);
            stmt.setString(56, paciente.getHora_sessoes());
            stmt.setString(57, paciente.getData());*/
            //stmt.executeUpdate();
            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
     public Integer InserirPacientesSessoes(Paciente paciente, String dias){
        //int id = 0;
        try {          
            String query = "INSERT INTO pacientesessoes"
                    +"("
                    + "idpacientes,"
                    /*+ "qtd_sessoes, "*/
                    + "dias_sessoes, "
                    + "hora_sessoes, "
                    + "data"
                    + " ) values (?,?,?,?)";
            
            PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS); // retorna o id do paciente
            stmt.setInt(1, paciente.getIdpacientes());
            /*stmt.setInt(2, paciente.getQtd_sessoes());*/
            stmt.setString(2, dias);
            stmt.setString(3, paciente.getHora_sessoes());
            stmt.setString(4, paciente.getData());
            //stmt.executeUpdate();
            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void updatePaciente(Paciente paciente) {
        String query = "UPDATE clinica.pacientes SET num_sus = '" + paciente.getNum_sus() + "', nome = '" + paciente.getNome() + "', telefone = '" + paciente.getTelefone() + "', dt_nasc = '" + paciente.getDt_nasc() + "', idade = '" + paciente.getIdade() + "', sexo = '" + paciente.getSexo() + "', rg = '" + paciente.getRg() + "', dt_emissao = '" + paciente.getDt_emissao() + "', org_emissor = '" + paciente.getOrg_emissor() + "', nome_pai = '" + paciente.getNome_pai() + "', nome_mae = '" + paciente.getNome_mae() + "', profissao = '" + paciente.getProfissao() + "', raca_cor = '" + paciente.getRaca_cor() + "', cep = '" + paciente.getCep() + "', endereco = '" + paciente.getEndereco() + "', numero_casa = '" + paciente.getNumero_casa() + "', bairro = '" + paciente.getBairro() + "', uf = '" + paciente.getUf() + "', cidade = '" + paciente.getCidade() + "', diag_clinico = '" + paciente.getDiag_clinico() + "', diag_fiso = '" + paciente.getDiag_fiso() + "', anamnese = '" + paciente.getAnamnese() + "', hma = '" + paciente.getHma() + "', hmp = '" + paciente.getHmp() + "', ant_hereditario = '" + paciente.getAnt_hereditario() + "', alg_cirurgia = '" + paciente.getAlg_cirurgia() + "', qual_cirurgia = '" + paciente.getQual_cirurgia() + "', tabagista = '" + paciente.getTabagista() + "', num_cigarros = '" + paciente.getNum_cigarros() + "', etilista = '" + paciente.getEtilista() + "', qtd_etilista = '" + paciente.getQtd_etilista() + "', sedentario = '" + paciente.getSedentario() + "', freq_sendentario = '" + paciente.getFreq_sendentario() + "', medicamentos = '" + paciente.getMedicamentos() + "', quais_medicamentos = '" + paciente.getQuais_medicamentos() + "', inicio_sintoma = '" + paciente.getInicio_sintoma() + "', mecanismo_sintoma = '" + paciente.getMecanismo_sintoma() + "', acomp_sintoma = '" + paciente.getAcomp_sintoma() + "', qual_sintoma = '" + paciente.getQual_sintoma() + "', localizacao_dor = '" + paciente.getLocalizacao_dor() + "', carater_dor = '" + paciente.getCarater_dor() + "', irradiacao_dor = '" + paciente.getIrradiacao_dor() + "', local_dor = '" + paciente.getLocal_dor() + "', movimento_dor = '" + paciente.getMovimento_dor() + "', qual_dor = '" + paciente.getQual_dor() + "', repouso_dor = '" + paciente.getRepouso_dor() + "', climatica_dor = '" + paciente.getClimatica_dor() + "', esforco_dor = '" + paciente.getEsforco_dor() + "', qual_esforco = '" + paciente.getQual_esforco() + "', escala_eva = '" + paciente.getEscala_eva() + "', inspecao_exame = '" + paciente.getInspecao_exame() + "', tonus_exame = '" + paciente.getTonus_exame() + "', carac_exame = '" + paciente.getCarac_exame() + "', qtd_sessoes = '" + paciente.getQtd_sessoes() + "' WHERE idpacientes = '" + paciente.getIdpacientes() + "'"; 
        /*String query = "update pacientes set "                
            + " '" + paciente.getNum_sus() + "',"
            + " '" + paciente.getNome() + "',"
            + " '" + paciente.getTelefone() + "',"
            + " '" + paciente.getDt_nasc() + "',"
            + " '" + paciente.getIdade() + "',"
            + " '" + paciente.getSexo() + "',"
            + " '" + paciente.getRg() + "',"
            + " '" + paciente.getDt_emissao() + "',"
            + " '" + paciente.getOrg_emissor() + "',"
            + " '" + paciente.getNome_pai() + "',"
            + " '" + paciente.getNome_mae() + "',"
            + " '" + paciente.getProfissao() + "',"
            + " '" + paciente.getRaca_cor() + "',"
            + " '" + paciente.getCep() + "',"
            + " '" + paciente.getEndereco() + "',"
            + " '" + paciente.getNumero_casa() + "',"
            + " '" + paciente.getBairro() + "',"
            + " '" + paciente.getUf() + "',"
            + " '" + paciente.getCidade() + "',"
            + " '" + paciente.getDiag_clinico() + "',"
            + " '" + paciente.getDiag_fiso() + "',"
            + " '" + paciente.getAnamnese() + "',"
            + " '" + paciente.getHma() + "',"
            + " '" + paciente.getHmp() + "',"
            + " '" + paciente.getAnt_hereditario() + "',"
            + " '" + paciente.getAlg_cirurgia() + "',"
            + " '" + paciente.getQual_cirurgia() + "',"
            + " '" + paciente.getTabagista() + "',"
            + " '" + paciente.getNum_cigarros() + "',"
            + " '" + paciente.getEtilista() + "',"
            + " '" + paciente.getQtd_etilista() + "',"
            + " '" + paciente.getSedentario() + "',"
            + " '" + paciente.getFreq_sendentario() + "',"
            + " '" + paciente.getMedicamentos() + "',"
            + " '" + paciente.getQuais_medicamentos() + "',"
            + " '" + paciente.getInicio_sintoma() + "',"
            + " '" + paciente.getMecanismo_sintoma() + "',"
            + " '" + paciente.getAcomp_sintoma() + "',"
            + " '" + paciente.getQual_sintoma() + "',"
            + " '" + paciente.getLocalizacao_dor() + "',"
            + " '" + paciente.getCarater_dor() + "',"
            + " '" + paciente.getIrradiacao_dor() + "',"
            + " '" + paciente.getLocal_dor() + "',"
            + " '" + paciente.getMovimento_dor() + "',"
            + " '" + paciente.getQual_dor() + "',"
            + " '" + paciente.getRepouso_dor() + "',"
            + " '" + paciente.getClimatica_dor() + "',"
            + " '" + paciente.getEsforco_dor() + "',"
            + " '" + paciente.getQual_esforco() + "',"
            + " '" + paciente.getEscala_eva() + "',"
            + " '" + paciente.getInspecao_exame() + "',"
            + " '" + paciente.getTonus_exame() + "',"
            + " '" + paciente.getCarac_exame() +
            "' where idpacientes = " + paciente.getIdpacientes() +" ";*/
        
        System.out.println(query);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Paciente getPaciente(int idpacientes) throws SQLException {
        //String query = "select * from pacientes where idpacientes='" + idpacientes + "'";
        String query = "SELECT * FROM clinica.pacientes pct INNER JOIN clinica.pacientesessoes AS pctss USING (idpacientes) WHERE pct.idpacientes ='" + idpacientes + "'";
        //ArrayList<Paciente> Paciente = new ArrayList<Paciente>();
        Statement stmt = connection.createStatement();
        //PreparedStatement stmt = connection.prepareStatement(query);
        //stmt.setInt(1, idpacientes);
        ResultSet res = stmt.executeQuery(query);
        if (res.next()) {
            Paciente paciente = new Paciente();
                paciente.setIdpacientes(res.getInt("idpacientes"));
                paciente.setNum_sus(res.getInt("num_sus"));
                paciente.setNome(res.getString("nome"));
                paciente.setTelefone(res.getString("telefone")); 
                paciente.setDt_nasc(res.getString("dt_nasc"));
                paciente.setIdade(res.getInt("idade"));
                paciente.setSexo(res.getString("sexo"));
                paciente.setRg(res.getInt("rg"));
                paciente.setDt_emissao(res.getString("dt_emissao"));
                paciente.setOrg_emissor(res.getString("org_emissor"));
                paciente.setNome_pai(res.getString("nome_pai"));
                paciente.setNome_mae(res.getString("nome_mae"));
                paciente.setProfissao(res.getString("profissao"));
                paciente.setRaca_cor(res.getString("raca_cor"));
                paciente.setCep(res.getString("cep"));
                paciente.setEndereco(res.getString("endereco"));
                paciente.setNumero_casa(res.getInt("numero_casa"));
                paciente.setBairro(res.getString("bairro"));
                paciente.setUf(res.getString("uf"));
                paciente.setCidade(res.getString("cidade"));
                paciente.setDiag_clinico(res.getString("diag_clinico"));
                paciente.setDiag_fiso(res.getString("diag_fiso"));
                paciente.setAnamnese(res.getString("anamnese"));
                paciente.setHma(res.getString("hma"));
                paciente.setHmp(res.getString("hmp"));
                paciente.setAnt_hereditario(res.getString("ant_hereditario"));
                paciente.setAlg_cirurgia(res.getInt("alg_cirurgia"));
                paciente.setQual_cirurgia(res.getString("qual_cirurgia"));
                paciente.setTabagista(res.getInt("tabagista"));
                paciente.setNum_cigarros(res.getString("num_cigarros"));
                paciente.setEtilista(res.getInt("etilista"));
                paciente.setQtd_etilista(res.getString("qtd_etilista"));
                paciente.setSedentario(res.getInt("sedentario"));
                paciente.setFreq_sendentario(res.getString("freq_sendentario"));
                paciente.setMedicamentos(res.getInt("medicamentos"));
                paciente.setQuais_medicamentos(res.getString("quais_medicamentos"));
                paciente.setInicio_sintoma(res.getString("inicio_sintoma"));
                paciente.setMecanismo_sintoma(res.getString("mecanismo_sintoma"));
                paciente.setAcomp_sintoma(res.getInt("acomp_sintoma"));
                paciente.setQual_sintoma(res.getString("qual_sintoma"));
                paciente.setLocalizacao_dor(res.getString("localizacao_dor"));
                paciente.setCarater_dor(res.getString("carater_dor"));
                paciente.setIrradiacao_dor(res.getInt("irradiacao_dor"));
                paciente.setLocal_dor(res.getString("local_dor"));
                paciente.setMovimento_dor(res.getInt("movimento_dor"));
                paciente.setQual_dor(res.getString("qual_dor"));
                paciente.setRepouso_dor(res.getInt("repouso_dor"));
                paciente.setClimatica_dor(res.getInt("climatica_dor"));
                paciente.setEsforco_dor(res.getInt("esforco_dor"));
                paciente.setQual_esforco(res.getString("qual_esforco"));
                paciente.setEscala_eva(res.getInt("escala_eva"));
                paciente.setInspecao_exame(res.getString("inspecao_exame"));
                paciente.setTonus_exame(res.getString("tonus_exame"));
                paciente.setCarac_exame(res.getString("carac_exame"));
                paciente.setQtd_sessoes(res.getInt("qtd_sessoes"));
                paciente.setHora_sessoes(res.getString("hora_sessoes"));
                
        return paciente;
        }
        return null;
    }
    
    
    public ArrayList<Paciente>listaPaciente() throws SQLException{
        String query = "SELECT pct.idpacientes AS idpacientes, pctss.idsessoes AS idsessoes, pct.num_sus AS num_sus, pct.nome AS nome, pctss.status AS status,"
                + "pctss.hora_sessoes AS hora_sessoes, date_format(pctss.data,'%d/%m/%Y') AS data "
                + "FROM clinica.pacientes as pct "
                + "INNER JOIN clinica.pacientesessoes as pctss USING (idpacientes) "
                + "WHERE pctss.data regexp CURDATE() "
                + "ORDER BY pctss.hora_sessoes";
        
        ArrayList<Paciente> listaPaciente = new ArrayList<Paciente>();
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(query);
        while (res.next()){
            Paciente paciente = new Paciente();
            paciente.setIdpacientes(res.getInt("idpacientes"));
            paciente.setIdsessoes(res.getInt("idsessoes"));
            paciente.setNum_sus(res.getInt("num_sus"));
            paciente.setNome(res.getString("nome"));
            paciente.setStatus(res.getString("status"));
            paciente.setHora_sessoes(res.getString("hora_sessoes"));
            paciente.setData(res.getString("data"));
            listaPaciente.add(paciente);
        }
        return listaPaciente;
    }
    
    
    public void removePaciente(String Idpacientes) {
        String query = "delete from clinica.pacientes where idpacientes= '"+ Idpacientes +"' ";
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void UpdateStatus(String Idsessoes, String status) {
        String query = "UPDATE clinica.pacientesessoes "
                + "SET status='"+ status +"' "
                + "WHERE idsessoes='"+ Idsessoes +"'";
        System.out.println(query);
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    
}