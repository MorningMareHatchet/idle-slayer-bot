class IdleSlayerBotService : AccessibilityService() {

    companion5 object { // или просто companion object
        private var instance: IdleSlayerBotService? = null
        fun isInstanceRunning(): Boolean = instance != null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        // Логика создания плавающей панели при старте службы...
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_SHOW_PANEL") {
            // Метод, который отвечает за отрисовку плавающего окна на экране через WindowManager
            showFloatingPanel()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        removeFloatingPanel()
    }
    
    private fun showFloatingPanel() {
        // Здесь код инициализации WindowManager и добавления View плавающей панели,
        // если она еще не создана.
    }
    
    private fun removeFloatingPanel() {
        // Удаление панели при выключении
    }
}
