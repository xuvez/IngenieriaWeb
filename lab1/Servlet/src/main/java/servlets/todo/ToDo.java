package servlets.hello;

import todo.Note;
import todo.NoteList;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.*;

import com.google.gson.Gson;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/notes" })
public class ToDo extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String func = req.getParameter("func");
		if (func.equals("all")) {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<html><head><title>Todas las tareas</title></head>"
					+ "<body><h1>Todas las tareas</h1>"
					+ "<table border=\"1\"><colgroup><col span=\"1\" style=\"background-color:blue\">"
					+ "</colgroup>"
					+ "<tr><th>ID</th>"
					+ "<th>Tarea</th>"
					+ "<th>Contexto</th>"
					+ "<th>Proyecto</th>"
					+ "<th>Prioridad</th></tr>"
					+ todasNotas()
					+ "</table><br/><a href=\"javascript:history.back()\">Atras</a></body></html>");
		}
		else {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			String task = req.getParameter("task");
			String context = req.getParameter("context");
			String project = req.getParameter("project");
			String priority = req.getParameter("priority");
			out.println("<html><head><title>Tareas</title></head>"
					+ "<body><h1>Tareas</h1>"
					+ "<table border=\"1\"><tr>"
					+ "<th>Tarea</th>"
					+ "<th>Contexto</th>"
					+ "<th>Proyecto</th>"
					+ "<th>Prioridad</th></tr>"
					+ buscarNotas(task, context, project, priority)
					+ "</table><br/><a href=\"javascript:history.back()\">Atras</a></body></html>");
		}
	}

import javax.xml.ws.WebServiceRef;
import helloservice.endpoint.HelloService;
import helloservice.endpoint.Hello;

	@WebServiceRef(wsdlLocation="http://localhost:8080/helloservice/hello?wsdl")
	static HelloService service;

	public static void main(String[] args) {
		try {
			HelloClient client = new HelloClient();
			client.doTest(args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void doTest(String[] args) {
		try {
			System.out.println("Retrieving the port from the following service: " + service);
			Hello port = service.getHelloPort();
			System.out.println("Invoking the sayHello operation on the port.");

			String name;
			if (args.length > 0) {
				name = args[0];
			} else {
				name = "No Name";
			}

			String response = port.sayHello(name);
			System.out.println(response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String todasNotas() {
		String res = "";
		Gson gson = new Gson();
		NoteList notes = null;
		File f = new File("notas.json");
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			notes = gson.fromJson(br, NoteList.class);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int i = 1;
		for (Note n : notes.getAllNotes()) {
			res = res + "<tr><td>" + i + "</td>"
					+ "<td>" + n.getTask() + "</td>"
					+ "<td>" + n.getContext() + "</td>"
					+ "<td>" + n.getProject() + "</td>"
					+ "<td>" + n.getPriority() + "</td></tr>";
			i++;
		}
		return res;
	}

	public static void main(String args[]) throws Exception {
		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		// Send SOAP Message to SOAP Server
		String url = "http://ws.cdyne.com/emailverify/Emailvernotestemail.asmx";
		SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);

		// print SOAP Response
		System.out.print("Response SOAP Message:");
		soapResponse.writeTo(System.out);

		soapConnection.close();
	}

	private static SOAPMessage createSOAPRequest() throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String serverURI = "http://localhost:8080/";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("todo", serverURI);

		/*
		Constructed SOAP Request Message:

		<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:example="http://ws.cdyne.com/">
			<SOAP-ENV:Header/>
			<SOAP-ENV:Body>
				<example:VerifyEmail>
					<example:email>mutantninja@gmail.com</example:email>
					<example:LicenseKey>123</example:LicenseKey>
				</example:VerifyEmail>
			</SOAP-ENV:Body>
		</SOAP-ENV:Envelope>

		*/

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("VerifyEmail", "example");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("email", "example");
		soapBodyElem1.addTextNode("mutantninja@gmail.com");
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("LicenseKey", "example");
		soapBodyElem2.addTextNode("123");

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", serverURI  + "VerifyEmail");

		soapMessage.saveChanges();

		/* Print the request message */
		System.out.print("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println();

		return soapMessage;
	}

	private String buscarNotas(String task, String context, String project, String priority) {
		String res = "";
		Gson gson = new Gson();
		NoteList notes = null;
		File f = new File("notas.json");
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			notes = gson.fromJson(br, NoteList.class);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (task == null) task = "";
		if (context == null) context = "";
		if (project == null) project = "";

		Integer prio = null;
		if (priority != null && !priority.equals("")) {
			try {
				prio = Integer.parseInt(priority);
			} catch (NumberFormatException e) {}
		}
		
		for (Note n : notes.getNotes(task, context, project, prio)) {
			res = res + "<tr>"
					+ "<td>" + n.getTask() + "</td>"
					+ "<td>" + n.getContext() + "</td>"
					+ "<td>" + n.getProject() + "</td>"
					+ "<td>" + n.getPriority() + "</td></tr>";
		}
		return res;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}