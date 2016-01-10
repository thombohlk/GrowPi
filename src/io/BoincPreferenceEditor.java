package io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cli.CLI;

public class BoincPreferenceEditor {
    
    public static final String PREF_NAME_MAX_CPU = "cpu_usage_limit";

    public static void insertOrUpdate(String prefName, String value) {
        try {
            // open document
            String filepath = IniReader.read("boinc", "global_prefs_override_path");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            NodeList settingTags = doc.getElementsByTagName(prefName);
            if (settingTags.getLength() > 0) {
                // update
                settingTags.item(0).setTextContent(value);
            } else {
                // insert
                Node globalPrefs = doc.getElementsByTagName("global_preferences").item(0);
                Element pref = doc.createElement(prefName);
                pref.appendChild(doc.createTextNode(value));
                globalPrefs.appendChild(pref);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
        
        reloadPreferences();
    }

    protected static void reloadPreferences() {
        CLI.execute("boinccmd --read_global_prefs_override", false);
    }
}
