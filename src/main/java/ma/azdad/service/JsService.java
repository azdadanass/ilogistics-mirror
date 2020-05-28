package ma.azdad.service;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class JsService {

	public String typeahead(String id, String source) {
		Typeahead typeahead = new Typeahead(id, source);
		return typeahead.generateCode();
	}

	static class Typeahead {
		private String id; // to put in class attribute
		private String[] source;
		private String sourceStr;

		public Typeahead(String id, String sourceStr) {
			super();
			this.id = id;
			this.sourceStr = sourceStr;
		}

		public String generateCode() {
			String result = "<script type=\"text/javascript\">";
			result += "jQuery(function($){var substringMatcher = function(strs) {return function findMatches(q, cb) {var matches, substringRegex;matches = [];substrRegex = new RegExp(q, 'i');$.each(strs, function(i, str) {if (substrRegex.test(str)) {matches.push({ value: str });}});cb(matches);}} \n";
			result += "$('input." + id + "').typeahead({hint: true,highlight: true,minLength: 1}, {name: 'states',displayKey: 'value',source: substringMatcher(" + getSourceStr() + ")});});";
			result += "</script>";
			return result;
		}

		public String getSourceStr() {
			if (sourceStr != null)
				return sourceStr;
			else {
				String[] tmp = Arrays.copyOf(source, source.length);
				for (int i = 0; i < tmp.length; i++)
					tmp[i] = "'" + tmp[i] + "'";
				return Arrays.toString(tmp);
			}
		}
	}

}