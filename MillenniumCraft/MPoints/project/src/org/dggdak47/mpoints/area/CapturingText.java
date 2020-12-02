package org.dggdak47.mpoints.area;

public class CapturingText {
	public final String capturedSymbol;
	public final String uncapturedSymbol;
	public final String capturingBlockedSymbol;
	public final String capturingPrefix;
	public final String capturingSuffix;
	
	public CapturingText(String capturedSymbol, String uncapturedSymbol, String capturingBlockedSymbol, String capturingPrefix, String capturingSuffix) {
		this.capturedSymbol = capturedSymbol;
		this.uncapturedSymbol = uncapturedSymbol;
		this.capturingBlockedSymbol = capturingBlockedSymbol;
		this.capturingPrefix = capturingPrefix;
		this.capturingSuffix = capturingSuffix;
	}
}
