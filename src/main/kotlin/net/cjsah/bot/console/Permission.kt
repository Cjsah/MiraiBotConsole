package net.cjsah.bot.console

enum class Permission(val level: Int) {
    USER(0),
    HELPER(1),
    ADMIN(2),
    OWNER(3);

    companion object {
        fun get(level: Int): Permission? {
            for (value in values()) {
                if (value.level == level) return value
            }
            return null
        }
    }



}