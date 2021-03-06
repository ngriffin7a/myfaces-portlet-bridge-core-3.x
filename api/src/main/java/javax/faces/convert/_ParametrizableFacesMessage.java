/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.faces.convert;

import java.text.MessageFormat;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/** 
 * This class encapsulates a FacesMessage to evaluate the label
 * expression on render response, where f:loadBundle is available
 * 
 * @author Leonardo Uribe (latest modification by $Author: skitching $)
 * @version $Revision: 676298 $ $Date: 2008-07-13 05:31:48 -0500 (Dom, 13 Jul 2008) $
 */
class _ParametrizableFacesMessage extends FacesMessage
{
    /**
     * 
     */
    private static final long serialVersionUID = 7792947730961657948L;

    private final Object _args[];
    private String _evaluatedDetail;
    private String _evaluatedSummary;
    private transient Object _evaluatedArgs[];
    private Locale _locale;

    public _ParametrizableFacesMessage(
            String summary, String detail, Object[] args, Locale locale)
    {
        super(summary, detail);
        if(locale == null) throw new NullPointerException("locale");
        _locale = locale;
        _args = args;
    }

    public _ParametrizableFacesMessage(FacesMessage.Severity severity,
            String summary, String detail, Object[] args, Locale locale)
    {
        super(severity, summary, detail);
        if(locale == null) throw new NullPointerException("locale");
        _locale = locale;
        _args = args;
    }

    public String getDetail()
    {
        if (_evaluatedArgs == null && _args != null)
        {
            evaluateArgs();
        }
        if (_evaluatedDetail == null)
        {
            MessageFormat format = new MessageFormat(super.getDetail(), _locale);
            _evaluatedDetail = format.format(_evaluatedArgs);
        }
        return _evaluatedDetail;
    }

    public void setDetail(String detail)
    {
        super.setDetail(detail);
        _evaluatedDetail = null;
    }
    
    public String getUnformattedDetail()
    {
        return super.getDetail();
    }

    public String getSummary()
    {
        if (_evaluatedArgs == null && _args != null)
        {
            evaluateArgs();
        }
        if (_evaluatedSummary == null)
        {
            MessageFormat format = new MessageFormat(super.getSummary(), _locale);
            _evaluatedSummary = format.format(_evaluatedArgs);
        }
        return _evaluatedSummary;
    }

    public void setSummary(String summary)
    {
        super.setSummary(summary);
        _evaluatedSummary = null;
    }
    
    public String getUnformattedSummary()
    {
        return super.getSummary();
    }

    private void evaluateArgs()
    {
        _evaluatedArgs = new Object[_args.length];
        FacesContext facesContext = null;
        for (int i = 0; i < _args.length; i++)
        {
            if (_args[i] == null)
            {
                continue;
            }
            else if (_args[i] instanceof ValueBinding)
            {
                if (facesContext == null)
                {
                    facesContext = FacesContext.getCurrentInstance();
                }
                _evaluatedArgs[i] = ((ValueBinding)_args[i]).getValue(facesContext);
            }
            else 
            {
                _evaluatedArgs[i] = _args[i];
            }
        }
    }
}
