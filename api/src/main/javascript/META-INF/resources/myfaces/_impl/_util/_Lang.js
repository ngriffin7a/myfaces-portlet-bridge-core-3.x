/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */


/*
 theoretically we could save some code
 by
 defining the parent object as
 var parent = new Object();
 parent.prototype = new myfaces._impl.core._Runtime();
 extendClass(function () {
 }, parent , {
 But for now we are not doing it the little bit of saved
 space is not worth the loss of readability
 */

//Intellij Warnings settings
/** @namespace myfaces._impl._util._Lang */
/** @namespace window.console */
 myfaces._impl.core._Runtime.singletonExtendClass("myfaces._impl._util._Lang", Object, {

    fetchNamespace : function(namespace) {
        if(!namespace || !this.isString(namespace)) {
            throw Error("_Lang.fetchNamespace namespace must be of type String");
        }
        return myfaces._impl.core._Runtime.fetchNamespace(namespace);
    },

    reserveNamespace : function(namespace) {
        if(!this.isString(namespace)) {
            throw Error("_Lang.reserveNamespace namespace must be of type String");
        }
        return myfaces._impl.core._Runtime.reserveNamespace(namespace);
    },

    globalEval : function(code) {
        if(!this.isString(code)) {
            throw Error("_Lang.globalEval code must be of type String");
        }
        return myfaces._impl.core._Runtime.globalEval(code);
    },

    /**
     * cross port from the dojo lib
     * browser save event resolution
     * @param evt the event object
     * (with a fallback for ie events if none is present)
     */
    getEventTarget: function(evt) {
        //ie6 and 7 fallback
        evt = (!evt) ? window.event || {} : evt;
        var t = (evt.srcElement ? evt.srcElement : (evt.target ? evt.target : null));
        while ((t) && (t.nodeType != 1)) {
            t = t.parentNode;
        }
        return t;
    },

    /**
     * check if an element exists in the root
     */
    exists : function(root, subNamespace) {
        return myfaces._impl.core._Runtime.exists(root, subNamespace);
    },

    /**
     @see myfaces._impl.core._Runtime.extendClass
     */
    singletonExtendClass : function(newClass, extendsClass, functionMap, inherited) {
        return myfaces._impl.core._Runtime.singletonExtendClass(newClass, extendsClass, functionMap, inherited);
    },


    /**
     * equalsIgnoreCase, case insensitive comparison of two strings
     *
     * @param source
     * @param destination
     */
    equalsIgnoreCase: function(source, destination) {
        //either both are not set or null
        if (!source && !destination) {
            return true;
        }
        //source or dest is set while the other is not
        if (!source || !destination) return false;

        //in any other case we do a strong string comparison
        return source.toLowerCase() === destination.toLowerCase();
    },


    /**
     @see myfaces._impl.core._Runtime.extendClass
     */
    extendClass : function(newClass, extendsClass, functionMap, inherited) {
        return myfaces._impl.core._Runtime.extendClass(newClass, extendsClass, functionMap, inherited);
    },

    //core namespacing and inheritance done, now to the language extensions

    /**
     * Save document.getElementById (this code was ported over from dojo)
     * the idea is that either a string or domNode can be passed
     * @param {Object} reference the reference which has to be byIded
     */
    byId : function(/*object*/ reference) {
        if (!reference) {
            throw Error("_Lang.byId , a reference node or identifier must be provided");
        }
        return (this.isString(reference)) ? document.getElementById(reference) : reference;
    },

    /**
     * backported from dojo
     * Converts an array-like object (i.e. arguments, DOMCollection) to an
     array. Returns a new Array with the elements of obj.
     * @param {Object} obj the object to "arrayify". We expect the object to have, at a
     minimum, a length property which corresponds to integer-indexed
     properties.
     * @param {int} offset the location in obj to start iterating from. Defaults to 0.
     Optional.
     * @param {Array} packArr An array to pack with the properties of obj. If provided,
     properties in obj are appended at the end of startWith and
     startWith is the returned array.
     */
    /*_toArray : function(obj, offset, packArr) {
     //	summary:
     //		Converts an array-like object (i.e. arguments, DOMCollection) to an
     //		array. Returns a new Array with the elements of obj.
     //	obj:
     //		the object to "arrayify". We expect the object to have, at a
     //		minimum, a length property which corresponds to integer-indexed
     //		properties.
     //	offset:
     //		the location in obj to start iterating from. Defaults to 0.
     //		Optional.
     //	startWith:
     //		An array to pack with the properties of obj. If provided,
     //		properties in obj are appended at the end of startWith and
     //		startWith is the returned array.
     var arr = packArr || [];
     //TODO add splicing here

     for (var x = offset || 0; x < obj.length; x++) {
     arr.push(obj[x]);
     }
     return arr; // Array
     }, */

    /**
     * Helper function to provide a trim with a given splitter regular expression
     * @param {|String|} it the string to be trimmed
     * @param {|RegExp|} splitter the splitter regular expressiion
     *
     * FIXME is this still used?
     */
    trimStringInternal : function(it, splitter) {
        return this.strToArray(it, splitter).join(splitter);
    },

    /**
     * String to array function performs a string to array transformation
     * @param {String} it the string which has to be changed into an array
     * @param {RegExp} splitter our splitter reglar expression
     * @return an array of the splitted string
     */
    strToArray : function(/*string*/ it, /*regexp*/ splitter) {
        //	summary:
        //		Return true if it is a String

        if (!this.isString(it)) {
            throw Error("myfaces._impl._util._Lang.strToArray param not of type string");
        }
        if (!splitter) {
            throw Error("myfaces._impl._util._Lang.strToArray a splitter param must be provided which is either a tring or a regexp");
        }
        var retArr = it.split(splitter);
        var len = retArr.length;
        for (var cnt = 0; cnt < len; cnt++) {
            retArr[cnt] = this.trim(retArr[cnt]);
        }
        return retArr;
    },

    /**
     * hyperfast trim
     * http://blog.stevenlevithan.com/archives/faster-trim-javascript
     * crossported from dojo
     */
    trim : function(/*string*/ str) {
       if(!this.isString(str)) {
            throw Error("_Lang.trim, parameter must be of type String");
        }
        str = str.replace(/^\s\s*/, '');
        var ws = /\s/;
        var i = str.length;
        while (ws.test(str.charAt(--i)));
        return str.slice(0, i + 1);
    },

    /**
     * Backported from dojo
     * a failsafe string determination method
     * (since in javascript String != "" typeof alone fails!)
     * @param it {|Object|} the object to be checked for being a string
     * @return true in case of being a string false otherwise�
     */
    isString: function(/*anything*/ it) {
        //	summary:
        //		Return true if it is a String
        return !!arguments.length && it != null && (typeof it == "string" || it instanceof String); // Boolean
    },
    /**
     * hitch backported from dojo
     * hitch allows to assign a function to a dedicated scope
     * this is helpful in situations when function reassignments
     * can happen
     * (notably happens often in lazy xhr code)
     *
     * @param {Function} scope of the function to be executed in
     * @param {Function} method to be executed
     *
     * @return whatevery the executed method returns
     */
    hitch : function(/*Object*/scope, /*Function|String*/method /*,...*/) {
        //	summary:
        //		Returns a function that will only ever execute in the a given scope.
        //		This allows for easy use of object member functions
        //		in callbacks and other places in which the "this" keyword may
        //		otherwise not reference the expected scope.
        //		Any number of default positional arguments may be passed as parameters
        //		beyond "method".
        //		Each of these values will be used to "placehold" (similar to curry)
        //		for the hitched function.
        //	scope:
        //		The scope to use when method executes. If method is a string,
        //		scope is also the object containing method.
        //	method:
        //		A function to be hitched to scope, or the name of the method in
        //		scope to be hitched.
        //	example:
        //	|	myfaces._impl._util._Lang.hitch(foo, "bar")();
        //		runs foo.bar() in the scope of foo
        //	example:
        //	|	myfaces._impl._util._Lang.hitch(foo, myFunction);
        //		returns a function that runs myFunction in the scope of foo
        if (arguments.length > 2) {
            return this._hitchArgs._hitchArgs.apply(this._hitchArgs, arguments); // Function
        }
        if (!method) {
            method = scope;
            scope = null;
        }
        if (this.isString(method)) {
            scope = scope || window || function() {
            };
            /*since we do not have dojo global*/
            if (!scope[method]) {
                throw(['myfaces._impl._util._Lang: scope["', method, '"] is null (scope="', scope, '")'].join(''));
            }
            return function() {
                return scope[method].apply(scope, arguments || []);
            }; // Function
        }
        return !scope ? method : function() {
            return method.apply(scope, arguments || []);
        }; // Function
    }
    ,

    _hitchArgs : function(scope, method /*,...*/) {
        var pre = this.objToArray(arguments, 2);
        var named = this.isString(method);
        return function() {
            // array-fy arguments
            var args = this.objToArray(arguments);
            // locate our method
            var f = named ? (scope || this.global)[method] : method;
            // invoke with collected args
            return f && f.apply(scope || this, pre.concat(args)); // mixed
        }; // Function
    }
    ,

    /**
     * Helper function to merge two maps
     * into one
     * @param {|Object|} dest the destination map
     * @param {|Object|} src the source map
     * @param {|boolean|} overwrite if set to true the destination is overwritten if the keys exist in both maps
     **/
    mixMaps : function(dest, src, overwrite) {
        if(!dest || !src) {
            throw Error("_Lang.mixMaps, both a source as well as a destination map must be provided");
        }

        /**
         * mixing code depending on the state of dest and the overwrite param
         */
        var ret = {};
        var keyIdx = {};
        var key = null;
        for (key in src) {
            /**
             *we always overwrite dest with source
             *unless overWrite is not set or source does not exist
             *but also only if dest exists otherwise source still is taken
             */
            if (!overwrite) {
                /**
                 *we use exists instead of booleans because we cannot rely
                 *on all values being non boolean, we would need an elvis
                 *operator in javascript to shorten this :-(
                 */
                ret[key] = this.exists(dest, key) ? dest[key] : src[key];
            } else {
                ret[key] = this.exists(src, key) ? src[key] : dest[key];
            }
            keyIdx[key] = true;
        }
        for (key in dest) {
            /*if result.key does not exist we push in dest.key*/
            ret[key] = this.exists(ret, key) ? ret[key] : dest[key];
        }
        return ret;
    }
    ,

    /**
     * checks if an array contains an element
     * @param {Array} arr   array
     * @param {String} str string to check for
     */
    contains : function(arr, str) {
        if(!arr || !str) {
            throw Error("_Lang.contains an array and a string must be provided");
        }

        for (var cnt = 0; cnt < arr.length; cnt++) {
            if (arr[cnt] == str) {
                return true;
            }
        }
        return false;
    }
    ,

    /**
     * Concatenates an array to a string
     * @param {Array} arr the array to be concatenated
     * @param {String} delimiter the concatenation delimiter if none is set \n is used
     *
     * @return the concatenated array, one special behavior to enable j4fry compatibility has been added
     * if no delimiter is used the [entryNumber]+entry is generated for a single entry
     * TODO check if this is still needed it is somewhat outside of the scope of the function
     * and functionality wise dirty
     */
    arrToString : function(/*String or array*/ arr, /*string*/ delimiter) {
        if(!arr) {
            throw Error("_Lang.arrayToString array must be set");
        }
        if (this.isString(arr)) {
            return arr;
        }

        delimiter = delimiter || "\n";
        return arr.join(delimiter);
    },

    /**
     * general type assertion routine
     *
     * @param probe the probe to be checked for the correct type
     * @param theType the type to be checked for
     */
    assertType : function(probe, theType) {
        return this.isString(theType) ? probe == typeof theType : probe instanceof theType;
    },

    /**
     * onload wrapper for chaining the onload cleanly
     * @param func the function which should be added to the load
     * chain (note we cannot rely on return values here, hence jsf.util.chain will fail)
     */
    addOnLoad: function(func) {
        var oldonload = window.onload;
        window.onload = (!this.assertType( window.onload, "function")) ? func : function() {
            oldonload();
            func();
        };
    },


    objToArray: function(obj, offset, pack) {
        //since offset is numeric we cannot use the shortcut due to 0 being false
        var finalOffset = ('undefined' != typeof offset || null != offset) ? offset : 0;
        var finalPack = pack || [];
        try {
            return finalPack.concat(Array.prototype.slice.call(obj, finalOffset));
        } catch (e) {
            //ie8 (again as only browser) delivers for css 3 selectors a non convertible object
            //we have to do it the hard way
            //ie8 seems generally a little bit strange in its behavior some
            //objects break the function is everything methodology of javascript
            //and do not implement apply call, or are pseudo arrays which cannot
            //be sliced
            for (var cnt = finalOffset; cnt < obj.length; cnt++) {
                finalPack.push(obj[cnt]);
            }
            return finalPack;
        }

    },

    //not yet used
    /**
     * attaches a standard iterator if
     * the collection does not have one already
     *
     * @param inColl
     */
    /*  attachIterators : function (inColl ) {
     var _coll = inColl;

     if(_coll instanceof Array) {
     if(!_coll.each) {
     _coll.each = function(closure) {
     for(var cnt = 0; cnt < _coll.length; cnt++) {
     closure(_coll[cnt]);
     }
     }
     }
     if(!_coll.filter) {
     _coll.filter = function(closure) {
     var retVal = [];
     for(var cnt = 0; cnt < _coll.length; cnt++) {
     var elem = closure(_coll[cnt]);
     if(closure(elem)) {
     retVal.push(elem)
     }
     }
     }
     }

     } else {
     if(!_coll.each) {
     _coll.each = function(closure) {
     for(var key in _coll.length) {
     closure(key, _coll[key]);
     }
     }
     }
     if(!_coll.filter) {
     _coll.filter = function(closure) {
     var retVal = [];
     for(var key in _coll.length) {
     var elem = closure(_coll[key]);
     if(closure(key, elem)) {
     retVal.push(elem)
     }
     }
     }
     }

     }
     },  */

    /**
     * helper to automatically apply a delivered arguments map or array
     * to its destination which has a field "_"<key> and a full field
     *
     * @param dest the destination object
     * @param args the arguments array or map
     * @param argNames the argument names to be transferred
     */
    applyArgs: function(dest, args, argNames) {
        if (argNames) {
            for (var cnt = 0; cnt < args.length; cnt++) {
                //dest can be null or 0 hence no shortcut
                if ('undefined' != typeof dest["_" + argNames[cnt]]) {
                    dest["_" + argNames[cnt]] = args[cnt];
                }
                if ('undefined' != typeof dest[ argNames[cnt]]) {
                    dest[argNames[cnt]] = args[cnt];
                }
            }
        } else {
            for (var key in args) {
                if ('undefined' != typeof dest["_" + key]) {
                    dest["_" + key] = args[key];
                }
                if ('undefined' != typeof dest[key]) {
                    dest[key] = args[key];
                }
            }
        }
    },
    /**
     * creates a standardized error message which can be reused by the system
     *
     * @param sourceClass the source class issuing the exception
     * @param func the function issuing the exception
     * @param error the error object itself (optional)
     */
    createErrorMsg: function(sourceClass, func, error) {
        var ret = [];

        ret.push(this.keyValToStr("Affected Class: ", sourceClass));
        ret.push(this.keyValToStr("Affected Method: ", func));

        if (error) {
            ret.push(this.keyValToStr("Error name: ", error.name ? error.name : "undefined"));
            ret.push(this.keyValToStr("Error message: ", error.message ? error.message : "undefined"));
            ret.push(this.keyValToStr("Error description: ", error.description ? error.description : "undefined"));
            ret.push(this.keyValToStr("Error number: ", 'undefined' != typeof error.number ? error.number : "undefined"));
            ret.push(this.keyValToStr("Error line number: ", 'undefined' != typeof error.lineNumber ? error.lineNumber : "undefined"));
        }
        return ret.join("");
    },

    /**
     * transforms a key value pair into a string
     * @param key the key
     * @param val the value
     * @param delimiter the delimiter
     */
    keyValToStr: function(key, val, delimiter) {
        var ret = [];
        ret.push(key);
        ret.push(val);
        if ('undefined' == typeof delimiter) {
            delimiter = "\n";
        }
        ret.push(delimiter);
        return ret.join("");
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