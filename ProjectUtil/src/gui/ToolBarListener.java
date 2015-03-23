package gui;

public interface ToolBarListener {
	public void textEmitted(String text);
	
	public void fireSaveFileChooser();

	public void fireLoadFileChooser();
}
