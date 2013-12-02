package com.simple.original.client.view.widgets;

import com.google.codemirror2_gwt.client.CodeMirrorConfig;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;

public class CodeEditorPanel extends FlowPanel implements LeafValueEditor<String>, IHandlesFullScreen {

    /**
     * Supported languages 
     * @author chinshaw
     *
     */
    public static enum CodeLanguage {
        R("text/x-rsrc"),
        C("text/x-csrc"),
        Java("text/x-java");
        
        /**
         * Code mirror mime type see.
         * 
         *  @see <a href="http://codemirror.net/">http://codemirror.net/</a>
         */
        private final String mimeType;
        
        CodeLanguage(String codeMirrorMimeType) {
            this.mimeType = codeMirrorMimeType;
        }
        
        public String getMimeType() {
            return mimeType;
        }
    }
    
    /**
     * The actual wrapper that 
     */
    private CodeMirrorWrapper codeMirrorWrapper = null;
    
    /**
     * This is our text area supplied by gwt.
     */
    private TextArea codeEditor = new TextArea();

    /**
     * Configuration for code mirror.
     */
    private CodeMirrorConfig config = CodeMirrorConfig.makeBuilder();
    
    /**
     * The current active language being edited.
     */
    private CodeLanguage lang;
    
    /**
     * Simple boolean for the editor being full screen.
     */
    private boolean isFullScreen;
    
    /**
     * Default constructor will initialize the editor to R code
     * editing. 
     */
    public CodeEditorPanel() {
        this(CodeLanguage.R);
    }
    
    /**
     * Our toolbar that has all of our standard buttons.
     */
    public CodeEditorPanel(CodeLanguage lang) {
        codeEditor.getElement().getStyle().setFontSize(100, Unit.PCT);
        // Have to add the editor to the dom before we can initialize the codemirror code.
        add(codeEditor);

        setLanguage(lang);
    }
    
    public CodeLanguage getLanguage() {
        return lang;
    }
    
    public void setLanguage(CodeLanguage lang) {
        this.lang = lang;
        config = config.setMode(lang.getMimeType()).setShowLineNumbers(true).setMatchBrackets(true).setShowGutter(true).setSmartIndent(true);

        TextAreaElement elem = codeEditor.getElement().cast();
        codeMirrorWrapper = CodeMirrorWrapper.createEditorFromTextArea(elem, config);
    }
    

    /**
     * Set the code
     */
    @Override
    public void setValue(String value) {
        if (value != null) {
            codeMirrorWrapper.setValue(value);
        }
    }

    /**
     * Get the code.
     */
    @Override
    public String getValue() {
        return codeMirrorWrapper.getValue();
    }

    @Override
    public void onFullScreen() {
        if (! isFullScreen) {
            PopupPanel pp = new PopupPanel(true);
            pp.setGlassEnabled(true);
            pp.setAnimationEnabled(true);
            
            
            pp.setWidget(codeEditor);
            pp.center();
        }        
    }

    @Override
    public void onCloseFullScreen() {
        // TODO Auto-generated method stub
        
    }
}