package framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;

import framework.data.dynamicValues.DynamicValuesHelper;
import framework.data.entities.LayoutFileData;
import framework.data.entities.LayoutFileField;
import framework.data.entities.LayoutFileStructure;

public class LayoutFile {
	
	public void testFile(LayoutFileData layoutFileData) throws Exception {
		DynamicValuesHelper dynamicValueUtils = new DynamicValuesHelper();
		dynamicValueUtils.changeDynamicPathAndFilename(layoutFileData);
		
		if(layoutFileData.PathAndFilename == null || layoutFileData.PathAndFilename.isEmpty())
			throw new Exception("Invalid PathAndFilename");
								
		if(!hasStructuresFieldIdentifer(layoutFileData.Structures))
			throw new Exception("File has more than one structure, but not all of them has an Field Identifider defined");
		
		List<String> errorMessages = new ArrayList<String>();
		
		List<String> lines = readFile(layoutFileData.PathAndFilename);
		
		List<LayoutFileStructure> structuresWithFieldIdentifier = new ArrayList<LayoutFileStructure>();
		if(layoutFileData.Structures.size() == 1) {
			errorMessages.addAll(validateLines(layoutFileData.Structures.get(0), layoutFileData.Delimiter, layoutFileData.FirstLineIsHeader, lines));
		} else {
			structuresWithFieldIdentifier = layoutFileData.Structures
					.stream()
					.filter(c -> c.StructureHasFieldIdentifier)
					.collect(Collectors.toList());
		
			for(LayoutFileStructure structure : structuresWithFieldIdentifier) {			
				List<String> filteredLines = new ArrayList<String>();
				
				if(layoutFileData.TypeFile.equals("sequential"))				
					filteredLines = lines
					.stream()
					.filter(c -> c.substring(structure.StructureFieldIdentifier.Start - 1, structure.StructureFieldIdentifier.Start - 1 + structure.StructureFieldIdentifier.Length).equals(structure.StructureFieldIdentifier.StructureValueIdentificator))
					.collect(Collectors.toList());
				else
					filteredLines = lines
					.stream()
					.filter(c -> c.split(layoutFileData.Delimiter)[structure.StructureFieldIdentifier.FieldIndex].indexOf(structure.StructureFieldIdentifier.StructureValueIdentificator) > -1)
					.collect(Collectors.toList());
				
				errorMessages.addAll(validateLines(structure, layoutFileData.Delimiter, layoutFileData.FirstLineIsHeader, filteredLines));
			}					
		}
		
		if(errorMessages.size() > 0)
			throw new Exception("Validation failed on file "+layoutFileData.TypeFileName+". Errors: " + errorMessages);
	}
	
	private List<String> validateLines(LayoutFileStructure structure, String delimiter, boolean firstLineIsHeader, List<String> lines) throws Exception{
		List<String> errorMessages = new ArrayList<String>();
		
		int i = 0;
		for(String line : lines)
		{
			if(firstLineIsHeader && i == 0) {
				i++;
				continue;	
			}
			
			List<String> fileFields = new ArrayList<String>();
			
			if(delimiter != null && !delimiter.isEmpty()) {
				fileFields = Arrays.asList(line.split(delimiter));
			} else {
				fileFields = getFieldsFromSequentialLine(line, structure.Fields);
			}
			
			if(fileFields.size() != structure.Fields.size())
				throw new Exception("File fields count must be equal Layout fields count");
			
			for(int k = 0; k <= fileFields.size() - 1; k++) {
				errorMessages.addAll(validateField(i, structure.StructureName, fileFields.get(k), structure.Fields.get(k)));
			}			

			i++;
		}
		
		return errorMessages;
	}
	
	private boolean hasStructuresFieldIdentifer(List<LayoutFileStructure> structures) {
		List<LayoutFileStructure> structuresWithFieldIdentifier = structures
				.stream()
				.filter(c -> c.StructureHasFieldIdentifier)
				.collect(Collectors.toList());
		
		return (structures.size() == 1) || 
			   (structures.size() > 1 && structuresWithFieldIdentifier.size() == structures.size());
	}
	
	private List<String> readFile(String pathAndFilename) throws Exception {
		List<String> lines = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(pathAndFilename));
		String line = reader.readLine();
		while (line != null) {
			lines.add(line);
			line = reader.readLine();
		}
		reader.close();
		
		return lines;
	}
	
	private List<String> getFieldsFromSequentialLine(String line, List<LayoutFileField> layoutFields){
		List<String> fileFields = new ArrayList<String>();
		
		for(LayoutFileField field : layoutFields) {
			fileFields.add(line.substring(field.Start - 1, field.Start - 1 + field.Length));
		}
		
		return fileFields;
	}
		
	private List<String> validateField(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) throws Exception {
		
		List<String> errorMessages = new ArrayList<String>();
		
		if(layoutField.AcceptNull && fileField.isEmpty())
			return errorMessages;
		
		errorMessages.addAll(validateFieldType(lineNumber, structureName, fileField, layoutField));
		
		if(!layoutField.AcceptNull)
			errorMessages.addAll(validateFieldNullable(lineNumber, structureName, fileField, layoutField));
				
		if(layoutField.FieldFormat != null && !layoutField.FieldFormat.isEmpty())
			errorMessages.addAll(validateFieldFormat(lineNumber, structureName, fileField, layoutField));
		
		if(layoutField.AcceptedValues.size() > 0)
			errorMessages.addAll(validateFieldAcceptedValues(lineNumber, structureName, fileField, layoutField));
		
		return errorMessages;
	}
	
	private List<String> validateFieldType(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) throws Exception {
		List<String> errorMessages = new ArrayList<String>();
		
		switch(layoutField.FieldType) {
			case "Alphanumeric":
				// do nothing. Accept all characters
				break;
			case "Alphabetic":
				errorMessages.addAll(validateIsAlphabetic(lineNumber, structureName, fileField, layoutField));
				break;
			case "Numeric":
				errorMessages.addAll(validateIsNumeric(lineNumber, structureName, fileField, layoutField));
				break;
			case "Date":
				errorMessages.addAll(validateIsDate(lineNumber, structureName, fileField, layoutField));
				break;
			case "Currency":
				errorMessages.addAll(validateIsCurrency(lineNumber, structureName, fileField, layoutField));
				break;
			case "CPF":
				errorMessages.addAll(validateIsCpf(lineNumber, structureName, fileField, layoutField));
				break;
			case "CNPJ":
				errorMessages.addAll(validateIsCnpj(lineNumber, structureName, fileField, layoutField));
				break;
			case "Email":
				errorMessages.addAll(validateIsEmail(lineNumber, structureName, fileField, layoutField));
				break;
			case "Yes/No":
				errorMessages.addAll(validateIsYesNo(lineNumber, structureName, fileField, layoutField));
				break;
			case "CPF/CNPJ":
				errorMessages.addAll(validateIsCpfCnpj(lineNumber, structureName, fileField, layoutField));
				break;
			default:
				throw new Exception("Invalid Layout Type Field");
		}
		
		return errorMessages;
	}
	
	private List<String> validateIsCpfCnpj(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		List<String> errorCpfMessages = new  ArrayList<String>();
		List<String> errorCnpjMessages = new  ArrayList<String>();
		
		errorCpfMessages.addAll(validateIsCpf(lineNumber, structureName, fileField, layoutField));		
		errorCnpjMessages.addAll(validateIsCnpj(lineNumber, structureName, fileField, layoutField));	
		
		if(errorCpfMessages.size() == 0 || errorCnpjMessages.size() == 0)
			return errorMessages;
		else {
			errorMessages.addAll(errorCpfMessages);
			errorMessages.addAll(errorCnpjMessages);
			return errorMessages;
		}
	}

	private List<String> validateIsYesNo(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		List<String> allowedValues = new ArrayList<String>();
		allowedValues.add("sim");
		allowedValues.add("si");
		allowedValues.add("sí");
		allowedValues.add("yes");
		allowedValues.add("não");
		allowedValues.add("nao");
		allowedValues.add("nao");
		allowedValues.add("true");
		allowedValues.add("verdadero");
		allowedValues.add("verdadeiro");
		allowedValues.add("false");
		allowedValues.add("falso");
		
		if(!allowedValues.contains(fileField.toLowerCase().trim()))
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with values "+fileField+" contains invalid values");
		
		return errorMessages;
	}

	private List<String> validateIsEmail(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("^(([^<>()[\\]\\\\.,;:\\s@\\\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\\\"]+)*)|(\\\".+\\\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");		
		Matcher matcher = pattern.matcher(fileField.trim());  
		if(!matcher.matches())
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid email");
		
		return errorMessages;
	}

	private List<String> validateIsCnpj(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		fileField = removeCaracteresEspeciais(fileField.trim());
		
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (fileField.equals("00000000000000") || 
			fileField.equals("11111111111111") || 
			fileField.equals("22222222222222") || 
			fileField.equals("33333333333333") || 
			fileField.equals("44444444444444") || 
			fileField.equals("55555555555555") || 
			fileField.equals("66666666666666") || 
			fileField.equals("77777777777777") || 
			fileField.equals("88888888888888") || 
			fileField.equals("99999999999999") || 
			(fileField.length() != 14)) {
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid CNPJ");
			return errorMessages;
		}

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = fileField.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = fileField.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if (!((dig13 == fileField.charAt(12)) && (dig14 == fileField.charAt(13)))) {
				errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid CNPJ");
				return errorMessages;
			}
		} catch (InputMismatchException erro) {
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid CNPJ");
			return errorMessages;
		}
		
		return errorMessages;
	}

	private List<String> validateIsCpf(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		fileField = removeCaracteresEspeciais(fileField.trim());
		
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (fileField.equals("00000000000") || fileField.equals("11111111111") ||
        	fileField.equals("22222222222") || fileField.equals("33333333333") ||
        	fileField.equals("44444444444") || fileField.equals("55555555555") ||
        	fileField.equals("66666666666") || fileField.equals("77777777777") ||
        	fileField.equals("88888888888") || fileField.equals("99999999999") ||
        	(fileField.length() != 11)) {
        	errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid CPF");
        	return errorMessages;
        }
            

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
        // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
        // converte o i-esimo caractere do CPF em um numero:
        // por exemplo, transforma o caractere '0' no inteiro 0
        // (48 eh a posicao de '0' na tabela ASCII)
            num = fileField.charAt(i) - 48;
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

        // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
            num = fileField.charAt(i) - 48;
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);

        // Verifica se os digitos calculados conferem com os digitos informados.
            if (!((dig10 == fileField.charAt(9)) && (dig11 == fileField.charAt(10)))){
            	errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid CPF");
            	return errorMessages;
            }
        } catch (InputMismatchException erro) {
        	errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid CPF");
        	return errorMessages;
        }
        
        return errorMessages;
    }

	private List<String> validateIsCurrency(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("([0-9]{1,3}\\.)*([0-9]{1,3}\\.)*[0-9]{1,3}\\,{0,1}[0-9]{0,2}");
		Matcher matcher = pattern.matcher(fileField.trim());  
		if(matcher.matches())
			return errorMessages;
		
		pattern = Pattern.compile("([0-9]{1,3}\\,)*([0-9]{1,3}\\,)*[0-9]{1,3}\\.{0,1}[0-9]{0,2}");
		
		matcher = pattern.matcher(fileField);  
		if(!matcher.matches())
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is not a valid currency");
		
		return errorMessages;
	}

	private List<String> validateIsNumeric(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("^[0-9]*$");		
		Matcher matcher = pattern.matcher(fileField.trim());  
		if(!matcher.matches())
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" contains not numeric characters");
		
		return errorMessages;
	}

	private List<String> validateIsDate(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) throws Exception{
		List<String> errorMessages = new ArrayList<String>();
		
		if(layoutField.DateFormat == null || layoutField.DateFormat.isEmpty())
			throw new Exception("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] does not have a date format specified");
		
		DateFormat sdf = new SimpleDateFormat(layoutField.DateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(fileField.trim());
        } catch (ParseException e) {
           errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" does not contain a valid Date");
        }		
        
        return errorMessages;
	}

	private List<String> validateIsAlphabetic(int lineNumber, String structureName, String fileField, LayoutFileField layoutField){
		List<String> errorMessages = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("^(([A-Za-zàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð]{0,99})([ ]{0,1})([-]{0,1})([.]{0,1})([*]{0,1})){0,99}$");		
		Matcher matcher = pattern.matcher(fileField.trim());  
		if(!matcher.matches())
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" contains not alphabetic characters");
		
		return errorMessages;		
	}

	private List<String> validateFieldNullable(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		if(fileField == null || fileField.isEmpty() || fileField.toLowerCase() == "null")
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value "+fileField+" is null");
		
		return errorMessages;
	}
	
	private List<String> validateFieldFormat(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) {
		List<String> errorMessages = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(layoutField.FieldFormat);		
		Matcher matcher = pattern.matcher(fileField);  
		if(!matcher.matches())
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value \""+fileField+"\" does not match the specified field format");
		
		return errorMessages;
	}
	
	private List<String> validateFieldAcceptedValues(int lineNumber, String structureName, String fileField, LayoutFileField layoutField) throws Exception {
		List<String> errorMessages = new ArrayList<String>();
		
		if(!layoutField.AcceptedValues.contains(fileField))
			errorMessages.add("Field ["+layoutField.FieldName+"] from structure ["+structureName+"] at line "+lineNumber+" with value \""+fileField+"\" does not contains the accepted values");
		
		return errorMessages;
	}
	
	private String removeCaracteresEspeciais(String doc) {
		if (doc.contains(".")) {
			doc = doc.replace(".", "");
		}
		if (doc.contains("-")) {
			doc = doc.replace("-", "");
		}
		if (doc.contains("/")) {
			doc = doc.replace("/", "");
		}
		return doc;
	}
}
