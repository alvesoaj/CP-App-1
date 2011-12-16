package br.com.bloum.android.contentprovider.app1;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.webkit.WebView;

public class App1 extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);

		// Obtém todos os contatos do celular
		Cursor contacts = managedQuery(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);

		// Se existe algum contato
		if (contacts.moveToFirst()) {

			String data = "<html><head><link href='css.css' rel='stylesheet' type='text/css' />"
					+ "<script type='text/javascript' src='jquery.js'></script>"
					+ "<script type='text/javascript' src='js.js'></script></head><body>";
			data = data.concat("<ul>");
			// Percorre cada contato
			do {
				// Iniciando a li (list item)
				data = data.concat("<li class='contact_name'>");

				// Id do contato
				String contactID = contacts.getString(contacts
						.getColumnIndex(ContactsContract.Contacts._ID));
				// Nome do contato (FirstName + LastName)
				String contactName = contacts.getString(contacts
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

				// exibir o nome do contato
				data = data.concat(contactName);

				data = data.concat("<div class='contact_numbers'><ul>");
				// String que identifica se o contato atual tem um número
				// cadastrado
				String hasPhone = contacts
						.getString(contacts
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				// Se tiver número irá buscar todos os números do contato
				if (hasPhone.equals("1")) {
					// Obtém todos os números do contato
					Cursor phones = managedQuery(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							// Join com a tabela de contatos
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactID, null, null);

					// Percorre cada número do contato
					while (phones.moveToNext()) {
						String contactNumber = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						data = data.concat("<li class='contact_number'>"
								+ contactNumber + "</li>");
					}
					phones.close();
				} else {
					data = data
							.concat("<li class='no_number'>----------------</li>");
				}
				data = data.concat("</ul></div></li>");
			} while (contacts.moveToNext());

			data = data.concat("</ul></body></html>");
			// Exibe tudo na webView
			webView.loadDataWithBaseURL("file:///android_asset/", data,
					"text/html", "utf-8", null);
			// Senão existir nenhum contato no telefone
		} else {
			webView.loadData("Nenhum contato encontrado", "text/html", "utf-8");
		}
		contacts.close();
	}
}