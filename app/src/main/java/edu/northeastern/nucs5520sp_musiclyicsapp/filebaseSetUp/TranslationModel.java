package edu.northeastern.nucs5520sp_musiclyicsapp.filebaseSetUp;

public class TranslationModel {
    String songId;
    String translationId;
    String language;
    String creator;

    public TranslationModel() {
    }

    public TranslationModel(String songId, String translationId, String language, String creator) {
        this.songId = songId;
        this.translationId = translationId;
        this.language = language;
        this.creator = creator;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
