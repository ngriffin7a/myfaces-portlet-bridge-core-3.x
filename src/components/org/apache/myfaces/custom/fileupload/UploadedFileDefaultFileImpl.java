/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.custom.fileupload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;


/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 *          $Log$
 *          Revision 1.1  2004/07/14 06:02:48  svieujot
 *          FileUpload : split file based and memory based implementation.
 *          Use the storage="memory|file" attribute.
 *          Default is memory because file based implementation fails to serialize.
 *
 *
 */
public class UploadedFileDefaultFileImpl extends UploadedFileDefaultImplBase
{

	private FileItem fileItem = null;


    public UploadedFileDefaultFileImpl()
    {
    }


    public UploadedFileDefaultFileImpl(FileItem fileItem) throws IOException
    {
    	this.fileItem = fileItem;

        _name = fileItem.getName();
        _contentType = fileItem.getContentType();
System.out.println("FILE");
    }


    /**
     * Answer the uploaded file contents.
     *
     * @return file contents
     */
    public byte[] getBytes() throws IOException
    {
    	byte[] bytes = new byte[(int) fileItem.getSize()];
        fileItem.getInputStream().read(bytes);
        return bytes;
    }


    /**
     * Answer the uploaded file contents input stream
     *
     * @return
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException
    {
    	return fileItem.getInputStream();
    }


    /**
     * Answer the size of this file.
     * @return
     */
    public long getSize() {
    	if( fileItem == null )
    		return 0;
    	return fileItem.getSize();
    }
}
