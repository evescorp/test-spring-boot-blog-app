(function () {
    var themeKey = "app-theme";

    function resolveTheme() {
        var storedTheme = null;
        try {
            storedTheme = localStorage.getItem(themeKey);
        } catch (e) {
            storedTheme = null;
        }

        if (storedTheme === "dark" || storedTheme === "light") {
            return storedTheme;
        }

        var prefersDark = window.matchMedia
            && window.matchMedia("(prefers-color-scheme: dark)").matches;
        return prefersDark ? "dark" : "light";
    }

    function persistTheme(theme) {
        try {
            localStorage.setItem(themeKey, theme);
        } catch (e) {
            // Ignore storage errors (private mode or blocked storage).
        }
    }

    function applyTheme(theme) {
        document.documentElement.setAttribute("data-theme", theme);
        var toggle = document.getElementById("theme-toggle");
        if (!toggle) {
            return;
        }

        var isDark = theme === "dark";
        toggle.setAttribute("aria-pressed", String(isDark));
        toggle.textContent = isDark ? "Light mode" : "Dark mode";
    }

    function toggleTheme() {
        var currentTheme = document.documentElement.getAttribute("data-theme") || resolveTheme();
        var nextTheme = currentTheme === "dark" ? "light" : "dark";
        applyTheme(nextTheme);
        persistTheme(nextTheme);
    }

    jQuery(document).ready(function () {
        applyTheme(resolveTheme());

        var toggle = document.getElementById("theme-toggle");
        if (!toggle) {
            return;
        }
        toggle.addEventListener("click", toggleTheme);
    });
})();
