package com.taobao.accs.eudemon;

public class SoData {
    private static final String ARM = "arm";
    private static final String ARMV7 = "arm";
    public static final String ARM_DATA = "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";
    private static final String MIPS = "mips";
    private static final String X86 = "x86";
    public static final String X86_DATA = "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////iwCJRCQw642Lg/z///+LAIlEJDTpfP///4uD/P///4sAiUQkLOlr////gEwkeALpYf///4uD/P///4sAiQQk6AH+//+JRCRs6Uj///+Lg/z///+NjCSMAAAAiUwkIInKiwDoPgMAAItMJCAx0oXAD0TRiVQkaOka////i4P8////iwCJRCR06Qn///+Lg/z///+LAIlEJFTp+P7//4BMJHgB6e7+//+Lg/z///+LAIlEJFDp3f7//4uD/P///4sAiUQkSOnM/v//i4P8////iwCJBCTobP3//4lEJGTps/7//4uD/P///4sAiUQkXOmi/v//i4P8////iwCJRCRM6ZH+//+Lg/z///+NTCR8iUwkIInKiwDoigIAAItMJCAx0oXAD0TRiVQkYOlm/v//i4P8////iwCJRCRY6VX+//+Lg/z///+LAIlEJHDpRP7//7gBAAAAi5P4////i4wknAAAAIsSOdEPhTsBAACNZfRbXl9dw4t8JDyJwYX/D4TrAAAAg3wkOAAPhOAAAACDfCQwAA+E1QAAAIN8JDQAD5TAD4THAAAA8q730YPBHokMJOif/P//hcCJxw+EwgAAAIk8JI2Dw+v//4lEJAyLRCQ8iUQkCI2D2ev//4lEJATogfz//8dEJAQAAAAAiTwk6IH8//+FwInGdH6JPCTog/z//4k0JI2D3uv//4lEJATogfz//4XAicd0fOiG/P//hcB4aWaQdA+JNCTohvz//zHA6SD///+LRCQkiUQkEItEJCyJRCQMi0QkKIlEJAiLRCQ0iUQkBItEJDiJBCT/14k0JOhO/P//McDp6P7//7j+////6d7+//+4/P///+nU/v//uP3////pyv7//7j6////6cD+//+JNCToF/z//7j7////6a7+///oyAAAAAAAAAAAAAAAjWQk9ItEJBCFwHQC/9CNZCQMw422AAAAAI28JwAAAABVieVT6LMAAACBw8sVAACD5PCNZCTgi4Po////iUQkFIuD7P///4lEJBiLg/D///+JRCQcjUQkFIlEJAyLg/T///+JRCQIx0QkBAAAAACNRQSJBCTol/r//420JgAAAABT6FYAAACBw24VAACNZCTojYNMAAAAiUQkCItEJCCJRCQEjYMM6v//iQQk6HD6//+NZCQYW8ONdgCNvCcAAAAAU+gWAAAAgcMuFQAAjWQk+Ohb+v//jWQkCFvDkJCQkJCQkJCQixwkwwAAAAAAAAAAV4nXVonGU+jg////gcP4FAAAjWQk8IkEJOgy+v//hcB0NoN4CAp0SItAEIsAiwCJBCToKfr//4lEJATHRCQIEAAAAIk8JOgl+v//McCNZCQQW15fw412AIk0JOjw+f//hcB1vo22AAAAAI2/AAAAALj/////69aQjbQmAAAAAIn2jbwnAAAAAAgAAAAEAAAAAQAAAEFuZHJvaWQAEwAAAHM6cDpuOmY6Yzp0OlA6SzpTOlU6RDpMOkk6TzpYOlk6QTpXOlRaAGxpYmNvY2tsb2dpYy0xLjEuMy5zbwAlcyVzAFJ1blRhc2sAAACX6P//qOj//6jo//+G6P//qOj//6jo//+o6P//qOj//1vo//+o6P//Suj//zno//+o6P//qOj//yDo//8P6P//qOj//6jo///+5///9Of//+Pn//+o6P//0uf//6Tn//+L5///gef//6jo//+o6P//qOj//6jo//+o6P//qOj//6jo//+o6P//cOf//6jo//+o6P//X+f//6jo//+o6P//qOj//6jo//+o6P//qOj//6jo//9R5///qOj//0Pn//+o6P//qOj//zXn//8f5///FAAAAAAAAAABelIAAXwIARsMBASIAQAAFAAAABwAAAA0/f//EwAAAABEDhBODgQAGAAAADQAAAA8/f//WQAAAABBDgiFAkINBUGDAxwAAABQAAAAgP3//zYAAAAAQQ4IgwJPDiBkDghBDgTDHAAAAHAAAACg/f//GwAAAABBDgiDAk8OEEkOCEEOBMMQAAAAkAAAAJz9//8MAAAAAAAAADQAAACkAAAAnP3//3cAAAAAQQ4IhwJDDgyGA0MOEIMETw4gewoOEEHDDgxBxg4IQccOBEQLAAAALAAAANwAAACE+P//6AMAAABBDgiFAkcNBUOHA4YEgwUDoQIKw0HGQcdBxQwEBEELJAAAAAwBAABE9///EAEAAAAOCEYODEoPC3QEeAA/GjsqMiQiAAAAAAAAAAABGwM7yP7//wgAAAAg9///1P///zD4//+k////IPz//+T+//9A/P///P7//6D8//8Y////4Pz//zj////8/P//WP///xD9//9s////AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA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";

    public static String getData(String str) {
        if (str.startsWith("arm")) {
            return ARM_DATA;
        }
        return X86.equals(str) ? X86_DATA : "";
    }
}
