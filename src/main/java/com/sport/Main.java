package com.sport;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Système de Gestion Sportive XML ===");

        // Validation XML/XSD
        boolean isValid = validateXML();
        System.out.println("Validation XML/XSD : " + (isValid ? "SUCCÈS" : "ÉCHEC"));

        // Interrogation XPath
        if (isValid) {
            executeXPathQueries();
        }
    }

    public static boolean validateXML() {
        try {
            // Création de la fabrique de schémas
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Chargement du schéma XSD
            Schema schema = factory.newSchema(new File("src/main/resources/sport.xsd"));

            // Création du validateur
            Validator validator = schema.newValidator();

            // Validation du fichier XML
            validator.validate(new StreamSource(new File("src/main/resources/sport.xml")));

            System.out.println("✅ Le document XML est valide selon le schéma XSD.");
            return true;

        } catch (SAXException e) {
            System.out.println("❌ Erreur de validation SAX : " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("❌ Erreur d'E/S : " + e.getMessage());
            return false;
        }
    }

    public static void executeXPathQueries() {
        try {
            // Chargement du document XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/main/resources/sport.xml"));

            // Création de l'objet XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            System.out.println("\n=== RÉSULTATS DES REQUÊTES XPATH ===");

            // Requête 1: Liste de tous les noms d'équipes
            System.out.println("\n1. Liste de toutes les équipes :");
            XPathExpression expr1 = xPath.compile("//equipe/nom");
            NodeList nodeList1 = (NodeList) expr1.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList1.getLength(); i++) {
                System.out.println("   - " + nodeList1.item(i).getTextContent());
            }

            // Requête 2: Équipes fondées avant 1900
            System.out.println("\n2. Équipes fondées avant 1900 :");
            XPathExpression expr2 = xPath.compile("//equipe[fondation < 1910]");
            NodeList nodeList2 = (NodeList) expr2.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList2.getLength(); i++) {
                String nom = xPath.compile("nom").evaluate(nodeList2.item(i));
                String fondation = xPath.compile("fondation").evaluate(nodeList2.item(i));
                System.out.println("   - " + nom + " (" + fondation + ")");
            }

            // Requête 3: Stades avec capacité > 60,000
            System.out.println("\n3. Stades de plus de 60,000 places :");
            XPathExpression expr3 = xPath.compile("//stade[capacite > 60000]");
            NodeList nodeList3 = (NodeList) expr3.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList3.getLength(); i++) {
                String nom = xPath.compile("nom").evaluate(nodeList3.item(i));
                String capacite = xPath.compile("capacite").evaluate(nodeList3.item(i));
                System.out.println("   - " + nom + " (" + capacite + " places)");
            }

            // Requête 4: Tous les stades
            System.out.println("\n4. Liste de tous les stades :");
            XPathExpression expr4 = xPath.compile("//stade/nom");
            NodeList nodeList4 = (NodeList) expr4.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList4.getLength(); i++) {
                System.out.println("   - " + nodeList4.item(i).getTextContent());
            }

            System.out.println("\n5. Équipes par ville :");
            XPathExpression expr5 = xPath.compile("//equipe");
            NodeList nodeList5 = (NodeList) expr5.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList5.getLength(); i++) {
                String nom = xPath.compile("nom").evaluate(nodeList5.item(i));
                String ville = xPath.compile("ville").evaluate(nodeList5.item(i));
                System.out.println("   - " + nom + " (" + ville + ")");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'interrogation XPath : " + e.getMessage());
        }
    }
}