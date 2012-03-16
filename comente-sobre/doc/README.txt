Vis�o geral das classes:

Componentes
- WebUser: Guarda as informa��es do usu�rio que est� acessando a aplica��o no momento atual
- EmailSender: Essa classe auxilia no envio de emails. Ela � usada pela classe SubmittedFileController, que precisa enviar um email para o respons�vel quando um arquivo � submetido para uma tarefa.
- UnixCommands: Antes de enviar um email, precisamos saber se a aplica��o est� localizada em algum servidor do IME. Isso porque, existe uma autentica��o para enviar emails. Para enviar emails por algum email do IME sem precisar de senha � preciso estar localizado dentro dele. Essa classe serve para auxiliar isso. Ela executa um comando unix que verifica qual � o dominio DNS do hospedeiro.

Daos - Todas as classes aqui servem de interm�dio da aplica��o com o banco de dados para os seus respectivos objetos no pacote Entity.

Factories - As tr�s classes presentes nesse pacote: ConfigurationCreator, SessionCreator e SessionFactoryCreator, s�o utilizadas para criar as sess�es do hibernate, para poder manipular o banco de dados.

Controllers - Classes Controller do padr�o MVC(Model-View-Controller)
- IndexController: Controla as requisi��es inicias da aplica��o, como incluir o objeto WebUser nas requisi��es.
- TaskController: Controla as requisi��es relativas as tarefas do sistema.
- UserController: Controla as requisi��es relaticas aos usu�rios do sistema. Como adicionar novo usu�rio, deletar e editar usu�rios existentes e fazer login.
- SubmittedFileController: Controla as requisi��es relativas aos arquivos enviados para cada tarefa do sistema. Essa classe recebe arquivos enviados pelos usu�rios, manda guardar, e salva o destino no banco de dados.
- EvaluationFormController: Serve para controlar as requisi��es de cria��o de formularios. <Por enquanto essa classe ainda n�o est� sendo utilizada>

Entities - Classes que controlam as entidades do sistema
- User: Class que guarda as informa��es dos usu�rios do sistema. A senha é encriptada.
- Task: Classe que guarda as informa��es das tarefas do sistema
- SubmittedFile: Classe que guarda as informa��es dos arquivos submetidos, e realizar a tarefa de salvar o arquivo no servidor.
- As classes EvaluationForm, EvaluationFormItem, MultipleChoiceEvaluationFormItem, NumericFieldEvaluationFormItem, TextFieldEvaluationFormItem ser�o utilizadas para prover ao respons�vel de uma tarefa a liberdade da cria��o do formul�rio para aquela tarefa.


