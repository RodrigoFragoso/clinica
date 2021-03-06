<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>S.I.F - Sistema Integrado de Fisioterapia</title>
        <link href="/clinica/sif/css/bootstrap.min.css" rel="stylesheet">
        <link href="/clinica/sif/css/sb-admin.css" rel="stylesheet">
        <link href="/clinica/sif/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">	
        <link href="/clinica/sif/css/plugins/morris.css" rel="stylesheet">
        <!--script src="/clinica/sif/js/bootstrap.min.js"></script>
        <script src="/clinica/sif/js/plugins/morris/raphael.min.js"></script>
        <script src="/clinica/sif/js/plugins/morris/morris.min.js"></script>
        <script src="/clinica/sif/js/plugins/morris/morris-data.js"></script-->
        <!--script src="jquery.js" type="text/javascript"></script-->
        <script src="/clinica/sif/js/jquery.js"></script>
        <script src="/clinica/sif/js/jquery.maskedinput.js" type="text/javascript"></script>
        <script src="/clinica/sif/js/bootstrap.min.js"></script>

        <!--. para class e # para id-->
        <script>
            jQuery(function ($) {
                $(".cpfMask").mask("999.999.999-99");
                $("#cep").mask("99999-999");
                $(".telefoneMask").mask("(99) 9999-9999");
            });
        </script>
    </head>

    <body>
        <div id="wrapper">    
            <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <a class="navbar-brand" href="/clinica/controllerAgenda">S.I.F - Sistema Integrado de Fisioterapia</a>
                </div>

                <!-- Top Menu Items -->
                <ul class="nav navbar-right top-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> ${dadosFuncionario.nome} <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="/clinica/LoginCli"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
                <div class="collapse navbar-collapse navbar-ex1-collapse">
                    <ul class="nav navbar-nav side-nav">
                        <li>
                            <a href="/clinica/controllerAgenda"><i class="fa fa-fw fa-table"></i> Agenda</a>
                        </li>
                        <li>
                            <a href="/clinica/sif/cadastraPaciente.jsp"><i class="fa fa-fw fa-edit"></i> Cadastro de Pacientes</a>
                        </li>
                        <!--li>
                            <a href="/clinica/sif/listaPaciente.jsp"><i class="glyphicon glyphicon-list-alt"></i> Pacientes</a>
                        </li-->
                        <li>
                            <a href="/clinica/sif/cadastraFuncionario.jsp"><i class="fa fa-fw fa-edit"></i> Cadastro de Funcion&aacute;rios</a>
                        </li>
                        <li class="active">
                            <a href="/clinica/controllerListaFunc"><i class="glyphicon glyphicon-list-alt"></i> Funcion&aacute;rios</a>
                        </li>
                        <li>
                            <a href="/clinica/sif/relatorios.jsp"><i class="fa fa-fw fa-bar-chart-o"></i> Relat&oacute;rios</a>
                        </li>
                        <li>
                            <a href="/clinica/LoginCli"><i class="glyphicon glyphicon-off"></i> SAIR</a>
                        </li>
                    </ul>
                </div>
                <!-- /.navbar-collapse -->
            </nav>

            <div id="page-wrapper">
                <ol class="breadcrumb">
                    <li><a href="#">In&iacute;cio</a></li>
                    <li class="active"><i class="glyphicon glyphicon-list-alt"></i> Funci&oacute;narios Cadastrados</a></li>
                </ol>
                <div class="alert alert-info" role="alert">
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    Atualize <b><u> o funcion&aacute;rio que deseja </u></b> e clique em <b>ATUALIZAR!</b>
                </div>
                <%
                    if (request.getSession().getAttribute("ok") == "FuncionarioCadastrado") {
                        request.getSession().removeAttribute("ok");
                %>
                <div class="alert alert-danger" role="alert">
                    <p align="center" style="color:red"><b>FUNCIONÁRIO CADASTRADO COM SUCESSO!</b></p>
                </div>
                <%}
                %>
                
                <%
                    if (request.getSession().getAttribute("ok") == "FuncionarioAtualizado") {
                        request.getSession().removeAttribute("ok");
                %>
                <div class="alert alert-danger" role="alert">
                    <p align="center" style="color:red"><b>FUNCIONÁRIO ATUALIZADO COM SUCESSO!</b></p>
                </div>
                <%}
                %>
                
                <%
                    if (request.getSession().getAttribute("ok") == "FuncionarioDelete") {
                        request.getSession().removeAttribute("ok");
                %>
                <div class="alert alert-danger" role="alert">
                    <p align="center" style="color:red"><b>FUNCIONÁRIO APAGADO COM SUCESSO!</b></p>
                </div>
                <%}
                %>

                <div class="panel panel-primary" id="panel">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-target="#collapseOne" href="#collapseOne">
                                <b> FUNCIÓNARIOS </b>
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse in">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped js-options-table">
                                <thead>
                                    <tr>
                                        <th>ID</th> 
                                        <th>NOME FUNCIONÁRIO</th> 
                                        <th>E-MAIL</th>
                                        <th>SENHA</th>
                                        <th>CPF</th>
                                        <th>CARGO</th>
                                        <th>TELEFONE</th>
                                        <th>AÇÕES</th>
                                    </tr>
                                </thead>
                                <tbody>

                                    <c:forEach items="${funcionarios}" var="panel">
                                    <form method="POST" action="/clinica/funcionarioController" name="func" id="func" role="form">     
                                        <tr>
                                            <td>
                                                <c:out value="${panel.idfuncionario}"/>
                                                <input type="hidden" value="${panel.idfuncionario}" name="idfuncionario">
                                                <input type="hidden" name="action" value="atualiza">
                                            </td>
                                            <td>
                                                <input type="text" name="nome" id="nome" value="<c:out value="${panel.nome}"/>">
                                            </td>
                                            <td>
                                                <input type="text" name="email" id="email" value="<c:out value="${panel.email}"/>">
                                            </td>
                                            <td>
                                                <input type="password" name="senha" id="senha" value="<c:out value="${panel.senha}"/>">
                                            </td>
                                            <td>
                                                <input type="text" class="cpfMask" name="cpf" id="cpf" value="<c:out value="${panel.cpf}"/>">
                                            </td>
                                            <td>
                                                <input type="text" name="cargo" id="cargo" value="<c:out value="${panel.cargo}"/>">
                                            </td>
                                            <td>
                                                <input type="text" class="telefoneMask" name="telefone" id="telefone" value="<c:out value="${panel.telefone}"/>">
                                            </td>
                                            <td>
                                                <button type="submit" class="btn btn-primary btn-xs">ATUALIZAR</button>
                                                <a class="btn btn-danger  btn-xs" href="funcionarioController?action=delete&idfuncionario=<c:out value="${panel.idfuncionario}"/>">EXCLUIR</a>
                                            </td>
                                        </tr>
                                    </form>    
                                </c:forEach>
                                </tbody>    
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>