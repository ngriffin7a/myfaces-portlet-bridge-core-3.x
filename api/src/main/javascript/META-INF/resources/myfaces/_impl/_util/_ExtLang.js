/**
 * debugging replacement for lang which adds logging functionality
 * which is not yet present in the core
 * this is a full replacement class for myfaces._impl._util._Lang
 * and replaces the object entirely with
 * a delegated implementation which adds the new methods to Lang
 *
 * We use this class to move some debugging related
 * lang functions out of the core, which never will
 * be utilized directly in the core
 * but will be used externally by extension frameworks
 * and by unit tests
 */
/** @namespace myfaces._impl._util._ExtLang */
myfaces._impl.core._Runtime.singletonDelegateObj("myfaces._impl._util._ExtLang", myfaces._impl._util._Lang, {


    constructor_: function() {
        //we replace the original one, and since we delegated
        //we have everything in place
        myfaces._impl._util._Lang = this;
    },

    /**
     * Simple simple logging only triggering at
     * firebug compatible logging consoles
     *
     * note: ;; means the code will be stripped
     * from the production code by the build system
     */
    _log: function(styleClass /*+arguments*/, args) {
        var logHolder = document.getElementById("myfaces.logging");
        if (logHolder) {
            var elem = document.createElement("div");
            //element.className = styleClass;
            elem.innerHTML = this.objToArray(arguments, 1).join(" ");
            logHolder.appendChild(elem);
        }
    },

    logLog: function(/*varargs*/) {
        var argStr = this.objToArray(arguments).join(" ");

        var c = window.console;
        if (c && c.log) {
            c.log(argStr);
        }
        this._log("logLog", "Log:" + argStr);
    },
    logDebug: function(/*varargs*/) {
        var argStr = this.objToArray(arguments).join(" ");
        var c = window.console;
        if (c && c.debug) {
            c.debug(argStr);
        }
        this._log("logDebug", "Debug:" + argStr);
    },
    logError: function(/*varargs*/) {
        var argStr = this.objToArray(arguments).join(" ");
        var c = window.console;
        if (c && c.error) {
            c.error(argStr);
        }
        this._log("logError", "Error:" + argStr);

    },
    logInfo: function(/*varargs*/) {
        var argStr = this.objToArray(arguments).join(" ");
        var c = window.console;
        if (c && c.info) {
            c.info(argStr);
        }
        this._log("logInfo", "Info:" + argStr);
    },
    logWarn: function(/*varargs*/) {
        var argStr = this.objToArray(arguments).join(" ");
        var c = window.console;
        if (c && c.warn) {
            c.warn(argStr);
        }
        this._log("logWarn", "Warn:" + argStr);
    }
});
