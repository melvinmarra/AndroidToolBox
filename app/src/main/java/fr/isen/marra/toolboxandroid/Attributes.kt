package fr.isen.marra.toolboxandroid


enum class Attributes(val uuid: String, val title: String) {
    SERVICE_INCONNU("", "Inconnu");
    //GENERIC_ACCESS("00001800-0000-1000-8000-00805f9b34fb", "Accès générique"),
    //GENERIC_ATTRIBUTE("00001801-0000-1000-8000-00805f9b34fb", "Attribut générique");

    companion object {
        fun getAttribute(uuid: String) = values().firstOrNull { it.uuid == uuid } ?: SERVICE_INCONNU
    }
}
