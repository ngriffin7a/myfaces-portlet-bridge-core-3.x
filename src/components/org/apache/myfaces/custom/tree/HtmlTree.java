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
package org.apache.myfaces.custom.tree;

import java.io.IOException;
import java.util.*;

import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.tree.event.TreeSelectionEvent;
import org.apache.myfaces.custom.tree.event.TreeSelectionListener;
import org.apache.myfaces.custom.tree.model.TreeModel;
import org.apache.myfaces.custom.tree.model.TreeModelEvent;
import org.apache.myfaces.custom.tree.model.TreeModelListener;
import org.apache.myfaces.custom.tree.model.TreePath;


/**
 * <p/>
 * Tree implementation based on javax.swing.JTree.
 * </p>
 * <p/>
 * The tree model is assigned by using a value binding named <code>model</code>
 * and is not stored in view state.
 * </p>
 * <p/>
 * A hierarchy of {@link HtmlTreeNode}objects is used to represent the current
 * expanded state of the tree. The root node is held as a faces named
 * <code>rootNode</code>.
 * </p>
 *
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller </a>
 * @version $Revision$ $Date$
 *          <p/>
 *          $Log$
 *          Revision 1.26  2004/11/26 12:33:54  oros
 *          MYFACES-9: store iconChildMiddle in component state
 *
 *          Revision 1.25  2004/11/26 12:14:10  oros
 *          MYFACES-8: applied tree table patch by David Le Strat
 *
 */
public class HtmlTree extends HtmlPanelGroup implements TreeModelListener
{

    public static final int DEFAULT_EXPIRE_LISTENERS = 8 * 60 * 60 * 1000; // 8 hours
    private static final String FACET_ROOTNODE = "rootNode";
    private static final String PREVIOUS_VIEW_ROOT = HtmlTree.class.getName() + ".PREVIOUS_VIEW_ROOT";
    private static final int EVENT_CHANGED = 0;
    private static final int EVENT_INSERTED = 1;
    private static final int EVENT_REMOVED = 2;
    private static final int EVENT_STRUCTURE_CHANGED = 3;
    private static int counter = 0;

    private IconProvider iconProvider;
    private boolean itemStatesRestored = false;
    private String var;
    private String styleClass;
    private String nodeClass;
    private String rowClasses;
    private String columnClasses;
    private String selectedNodeClass;
    private String iconClass;
    private String iconLine = "images/tree/line.gif";
    private String iconNoline = "images/tree/noline.gif";
    private String iconChild = "images/tree/noline.gif";
    private String iconChildFirst = "images/tree/line_first.gif";
    private String iconChildMiddle = "images/tree/line_middle.gif";
    private String iconChildLast = "images/tree/line_last.gif";
    private String iconNodeOpen = "images/tree/node_open.gif";
    private String iconNodeOpenFirst = "images/tree/node_open_first.gif";
    private String iconNodeOpenMiddle = "images/tree/node_open_middle.gif";
    private String iconNodeOpenLast = "images/tree/node_open_last.gif";
    private String iconNodeClose = "images/tree/node_close.gif";
    private String iconNodeCloseFirst = "images/tree/node_close_first.gif";
    private String iconNodeCloseMiddle = "images/tree/node_close_middle.gif";
    private String iconNodeCloseLast = "images/tree/node_close_last.gif";
    private int uniqueIdCounter = 0;
    private int[] selectedPath;
    private int internalId;
    private long expireListeners = DEFAULT_EXPIRE_LISTENERS;


    /**
     * <p/>
     * Default constructor.
     * </p>
     */
    public HtmlTree()
    {
        internalId = counter++;
    }


    public TreeModel getModel(FacesContext context)
    {
        ValueBinding binding = getValueBinding("model");

        if (binding != null)
        {
            TreeModel model = (TreeModel) binding.getValue(context);
            if (model != null)
            {
                return model;
            }
        }
        return null;
    }


    public String createUniqueId(FacesContext context)
    {
        return getClientId(context) + "_node_" + uniqueIdCounter++;
    }


    public void addTreeSelectionListener(TreeSelectionListener listener)
    {
        addFacesListener(listener);
    }


    public IconProvider getIconProvider()
    {
        return iconProvider;
    }


    public void setIconProvider(IconProvider iconProvider)
    {
        this.iconProvider = iconProvider;
    }


    /**
     * @return Returns the var.
     */
    public String getVar()
    {
        return var;
    }


    /**
     * @param var The var to set.
     */
    public void setVar(String var)
    {
        this.var = var;
    }


    public String getIconLine()
    {
        return iconLine;
    }


    public void setIconLine(String iconLine)
    {
        this.iconLine = iconLine;
    }


    public String getIconNoline()
    {
        return iconNoline;
    }


    public void setIconNoline(String iconNoline)
    {
        this.iconNoline = iconNoline;
    }


    public String getIconChild()
    {
        return iconChild;
    }


    public void setIconChild(String iconChild)
    {
        this.iconChild = iconChild;
    }


    public String getIconChildFirst()
    {
        return iconChildFirst;
    }


    public void setIconChildFirst(String iconChildFirst)
    {
        this.iconChildFirst = iconChildFirst;
    }


    public String getIconChildMiddle()
    {
        return iconChildMiddle;
    }


    public void setIconChildMiddle(String iconChildMiddle)
    {
        this.iconChildMiddle = iconChildMiddle;
    }


    public String getIconChildLast()
    {
        return iconChildLast;
    }


    public void setIconChildLast(String iconChildLast)
    {
        this.iconChildLast = iconChildLast;
    }


    public String getIconNodeOpen()
    {
        return iconNodeOpen;
    }


    public void setIconNodeOpen(String iconNodeOpen)
    {
        this.iconNodeOpen = iconNodeOpen;
    }


    public String getIconNodeOpenFirst()
    {
        return iconNodeOpenFirst;
    }


    public void setIconNodeOpenFirst(String iconNodeOpenFirst)
    {
        this.iconNodeOpenFirst = iconNodeOpenFirst;
    }


    public String getIconNodeOpenMiddle()
    {
        return iconNodeOpenMiddle;
    }


    public void setIconNodeOpenMiddle(String iconNodeOpenMiddle)
    {
        this.iconNodeOpenMiddle = iconNodeOpenMiddle;
    }


    public String getIconNodeOpenLast()
    {
        return iconNodeOpenLast;
    }


    public void setIconNodeOpenLast(String iconNodeOpenLast)
    {
        this.iconNodeOpenLast = iconNodeOpenLast;
    }


    public String getIconNodeClose()
    {
        return iconNodeClose;
    }


    public void setIconNodeClose(String iconNodeClose)
    {
        this.iconNodeClose = iconNodeClose;
    }


    public String getIconNodeCloseFirst()
    {
        return iconNodeCloseFirst;
    }


    public void setIconNodeCloseFirst(String iconNodeCloseFirst)
    {
        this.iconNodeCloseFirst = iconNodeCloseFirst;
    }


    public String getIconNodeCloseMiddle()
    {
        return iconNodeCloseMiddle;
    }


    public void setIconNodeCloseMiddle(String iconNodeCloseMiddle)
    {
        this.iconNodeCloseMiddle = iconNodeCloseMiddle;
    }


    public String getIconNodeCloseLast()
    {
        return iconNodeCloseLast;
    }


    public void setIconNodeCloseLast(String iconNodeCloseLast)
    {
        this.iconNodeCloseLast = iconNodeCloseLast;
    }


    public String getStyleClass()
    {
        return styleClass;
    }


    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }


    public String getNodeClass()
    {
        return nodeClass;
    }


    public void setNodeClass(String nodeClass)
    {
        this.nodeClass = nodeClass;
    }


    /**
     * @return Returns the rowClasses.
     */
    public String getRowClasses()
    {
        return rowClasses;
    }


    /**
     * @param rowClasses The rowClasses to set.
     */
    public void setRowClasses(String rowClasses)
    {
        this.rowClasses = rowClasses;
    }


    /**
     * @return Returns the columnClasses.
     */
    public String getColumnClasses()
    {
        return columnClasses;
    }


    /**
     * @param columnClasses The columnClasses to set.
     */
    public void setColumnClasses(String columnClasses)
    {
        this.columnClasses = columnClasses;
    }


    /**
     * @return Returns the selectedNodeClass.
     */
    public String getSelectedNodeClass()
    {
        return selectedNodeClass;
    }


    /**
     * @param selectedNodeClass The selectedNodeClass to set.
     */
    public void setSelectedNodeClass(String selectedNodeClass)
    {
        this.selectedNodeClass = selectedNodeClass;
    }


    public String getIconClass()
    {
        return iconClass;
    }


    public void setIconClass(String iconClass)
    {
        this.iconClass = iconClass;
    }


    public long getExpireListeners()
    {
        return expireListeners;
    }


    public void setExpireListeners(long expireListeners)
    {
        this.expireListeners = expireListeners;
    }


    public String getFamily()
    {
        return "org.apache.myfaces.HtmlTree";
    }


    /**
     * Ensures that the node identified by the specified path is expanded and
     * viewable. If the last item in the path is a leaf, this will have no
     * effect.
     *
     * @param path the <code>TreePath</code> identifying a node
     */
    public void expandPath(TreePath path, FacesContext context)
    {
        // Only expand if not leaf!
        TreeModel model = getModel(context);

        if (path != null && model != null && !model.isLeaf(path.getLastPathComponent()))
        {
            int[] translatedPath = HtmlTreeNode.translatePath(path, getModel(context));
            HtmlTreeNode rootNode = getRootNode();
            if (rootNode == null)
            {
                createRootNode(context);
                rootNode = getRootNode();
            }
            if (!rootNode.isExpanded())
            {
                rootNode.setExpanded(true);
            }
            rootNode.expandPath(translatedPath, 0);

        }
    }


    /**
     * Ensures that the node identified by the specified path is collapsed and
     * viewable.
     *
     * @param path the <code>TreePath</code> identifying a node
     */
    public void collapsePath(TreePath path, FacesContext context)
    {
        HtmlTreeNode node = findNode(path, context);

        if (node != null)
        {
            node.setExpanded(false);
        }
    }


    public boolean isExpanded(TreePath path, FacesContext context)
    {
        if (path == null)
        {
            return false;
        }

        return findNode(path, context) != null;
    }


    private HtmlTreeNode findNode(TreePath path, FacesContext context)
    {
        HtmlTreeNode node = getRootNode();
        int[] translatedPath = HtmlTreeNode.translatePath(path, getModel(context));

        for (int i = 0; i < translatedPath.length; i++)
        {
            if (!node.isExpanded())
            {
                return null;
            }
            int index = translatedPath[i];
            node = (HtmlTreeNode) node.getChildren().get(index);
        }
        return node;
    }


    public TreePath getSelectionPath()
    {
        if (selectedPath == null)
        {
            return null;
        }
        return HtmlTreeNode.translatePath(selectedPath, getModel(FacesContext.getCurrentInstance()));
    }


    public void selectionChanged(HtmlTreeNode node)
    {
        TreePath oldPath = null;

        if (selectedPath != null)
        {
            oldPath = HtmlTreeNode.translatePath(selectedPath, getModel(FacesContext.getCurrentInstance()));
        }
        selectedPath = node.getTranslatedPath();
        if (node.isSelected())
        {
            queueEvent(new TreeSelectionEvent(this, oldPath, node.getPath()));
        } else
        {
            queueEvent(new TreeSelectionEvent(this, oldPath, null));
        }
    }


    private void createRootNode(FacesContext context)
    {
        HtmlTreeNode node;
        TreeModel model = getModel(context);
        Object root = model.getRoot();
        node = (HtmlTreeNode) context.getApplication().createComponent(HtmlTreeNode.COMPONENT_TYPE);
        String id = createUniqueId(context);
        node.setId(id);

        node.setPath(new TreePath(new Object[]{root}));
        node.setUserObject(root);
        node.setLayout(new int[]{HtmlTreeNode.CLOSED_SINGLE});
        getFacets().put(FACET_ROOTNODE, node);
    }


    public HtmlTreeNode getRootNode()
    {
        return (HtmlTreeNode) getFacet(FACET_ROOTNODE);
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[26];
        values[0] = super.saveState(context);
        values[1] = iconChild;
        values[2] = iconChildFirst;
        values[3] = iconChildMiddle;
        values[4] = iconChildLast;
        values[5] = iconLine;
        values[6] = iconNodeClose;
        values[7] = iconNodeCloseFirst;
        values[8] = iconNodeCloseLast;
        values[9] = iconNodeCloseMiddle;
        values[10] = iconNodeOpen;
        values[11] = iconNodeOpenFirst;
        values[12] = iconNodeOpenLast;
        values[13] = iconNodeOpenMiddle;
        values[14] = iconNoline;
        values[15] = styleClass;
        values[16] = nodeClass;
        values[17] = selectedNodeClass;
        values[18] = new Integer(uniqueIdCounter);
        values[19] = selectedPath;
        values[20] = iconClass;
        values[21] = new Integer(internalId);
        values[22] = new Long(expireListeners);
        values[23] = rowClasses;
        values[24] = columnClasses;
        values[25] = var;
        return ((Object) (values));
    }


    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        iconChild = (String) values[1];
        iconChildFirst = (String) values[2];
        iconChildMiddle = (String) values[3];
        iconChildLast = (String) values[4];
        iconLine = (String) values[5];
        iconNodeClose = (String) values[6];
        iconNodeCloseFirst = (String) values[7];
        iconNodeCloseLast = (String) values[8];
        iconNodeCloseMiddle = (String) values[9];
        iconNodeOpen = (String) values[10];
        iconNodeOpenFirst = (String) values[11];
        iconNodeOpenLast = (String) values[12];
        iconNodeOpenMiddle = (String) values[13];
        iconNoline = (String) values[14];
        styleClass = (String) values[15];
        nodeClass = (String) values[16];
        selectedNodeClass = (String) values[17];
        uniqueIdCounter = ((Integer) values[18]).intValue();
        selectedPath = (int[]) values[19];
        iconClass = (String) values[20];
        internalId = ((Integer) values[21]).intValue();
        expireListeners = ((Long) values[22]).longValue();
        rowClasses = (String) values[23];
        columnClasses = (String) values[24];
        var = (String) values[25];
        addToModelListeners();
    }


    public void decode(FacesContext context)
    {
        super.decode(context);

        //Save the current view root for later reference...
        context.getExternalContext().getRequestMap().put(PREVIOUS_VIEW_ROOT, context.getViewRoot());
        //...and remember that this instance needs NO special treatment on
        // rendering:
        itemStatesRestored = true;
    }


    public void processDecodes(FacesContext context)
    {
        addToModelListeners();
        super.processDecodes(context);
    }


    public void processValidators(FacesContext context)
    {
        addToModelListeners();
        super.processValidators(context);
    }


    public void processUpdates(FacesContext context)
    {
        addToModelListeners();
        super.processUpdates(context);
    }


    public void encodeBegin(FacesContext context) throws IOException
    {
        addToModelListeners();
        processModelEvents();

        HtmlTreeNode node = getRootNode();

        if (node == null)
        {
            createRootNode(context);
        }

        if (!itemStatesRestored)
        {
            UIViewRoot previousRoot = (UIViewRoot) context.getExternalContext().getRequestMap().get(PREVIOUS_VIEW_ROOT);
            if (previousRoot != null)
            {
                restoreItemStates(context, previousRoot);
            } else
            {
                //no previous root, means no decode was done
                //--> a new request
            }
        }

        super.encodeBegin(context);
    }


    public void encodeEnd(FacesContext context) throws IOException
    {
        super.encodeEnd(context);
    }


    public void restoreItemStates(FacesContext facesContext, UIViewRoot previousRoot)
    {
        HtmlTree previousTree = (HtmlTree) previousRoot.findComponent(getClientId(facesContext));

        if (previousTree != null)
        {
            HtmlTreeNode node = previousTree.getRootNode();

            if (node != null)
            {
                getRootNode().restoreItemState(node);
            }

            selectedPath = previousTree.selectedPath;
        }
    }


    public void treeNodesChanged(TreeModelEvent e)
    {
        TreePath path = e.getTreePath();
        FacesContext context = FacesContext.getCurrentInstance();
        HtmlTreeNode node = findNode(path, context);

        if (node != null)
        {
            node.childrenChanged(e.getChildIndices(), context);
        }
    }


    public void treeNodesInserted(TreeModelEvent e)
    {
        TreePath path = e.getTreePath();
        FacesContext context = FacesContext.getCurrentInstance();
        HtmlTreeNode node = findNode(path, context);

        if (node != null)
        {
            node.childrenAdded(e.getChildIndices(), context);
        }
    }


    public void treeNodesRemoved(TreeModelEvent e)
    {
        TreePath path = e.getTreePath();
        FacesContext context = FacesContext.getCurrentInstance();
        HtmlTreeNode node = findNode(path, context);

        if (node != null)
        {
            node.childrenRemoved(e.getChildIndices());
        }
    }


    public void treeStructureChanged(TreeModelEvent e)
    {
        TreePath path = e.getTreePath();
        FacesContext context = FacesContext.getCurrentInstance();

        if (isExpanded(path, context))
        {
            collapsePath(path, context);
            expandPath(path, context);
        }
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof HtmlTree))
        {
            return false;
        }
        HtmlTree other = (HtmlTree) obj;

        return other.getId().equals(getId());
    }


    public int hashCode()
    {
        return getClientId(FacesContext.getCurrentInstance()).hashCode();
    }


    public void addToModelListeners()
    {
        Collection listeners = getModel(FacesContext.getCurrentInstance()).getTreeModelListeners();
        long currentTime = System.currentTimeMillis();
        boolean found = false;

        for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            ModelListener listener = (ModelListener) iterator.next();

            if (listener.getId() == internalId)
            {
                found = true;
            } else if (currentTime - listener.getLastAccessTime() > expireListeners)
            {
                iterator.remove();
            }
        }
        if (!found)
        {
            listeners.add(new ModelListener(internalId));
        }
    }


    private void processModelEvents()
    {
        Collection listeners = getModel(FacesContext.getCurrentInstance()).getTreeModelListeners();

        for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
        {
            ModelListener listener = (ModelListener) iterator.next();

            if (listener.getId() == internalId)
            {
                for (Iterator events = listener.getEvents().iterator(); events.hasNext();)
                {
                    Event event = (Event) events.next();
                    event.process(this);
                    events.remove();
                }
                break;
            }
        }
    }


    public void collapseAll()
    {
        HtmlTreeNode root = getRootNode();
        FacesContext context = FacesContext.getCurrentInstance();
        collapsePath(root.getPath(), context);
        for (int i = 0; i < root.getChildren().size(); i++)
        {
            HtmlTreeNode child = (HtmlTreeNode) (root.getChildren().get(i));
            collapsePath(child.getPath(), context);
            if (!child.isLeaf(context))
            {
                collapseChildren(context, child);
            }
        }
    }


    private void collapseChildren(FacesContext context, HtmlTreeNode parent)
    {
        for (int i = 0; i < parent.getChildren().size(); i++)
        {
            HtmlTreeNode child = (HtmlTreeNode) (parent.getChildren().get(i));
            collapsePath(child.getPath(), context);
            if (!child.isLeaf(context))
            {
                collapseChildren(context, child);
            }
        }

    }


    public void expandAll()
    {
        HtmlTreeNode root = getRootNode();
        FacesContext context = FacesContext.getCurrentInstance();
        expandPath(root.getPath(), context);
        for (int i = 0; i < root.getChildren().size(); i++)
        {
            HtmlTreeNode child = (HtmlTreeNode) (root.getChildren().get(i));
            expandPath(child.getPath(), context);
            if (!child.isLeaf(context))
            {
                expandChildren(context, child);
            }
        }
    }


    private void expandChildren(FacesContext context, HtmlTreeNode parent)
    {
        for (int i = 0; i < parent.getChildren().size(); i++)
        {
            HtmlTreeNode child = (HtmlTreeNode) (parent.getChildren().get(i));
            expandPath(child.getPath(), context);
            if (!child.isLeaf(context))
            {
                expandChildren(context, child);
            }
        }
    }


    private static class ModelListener implements TreeModelListener
    {

        private long lastAccessTime = System.currentTimeMillis();

        private LinkedList events = new LinkedList();

        int id;


        public ModelListener(int id)
        {
            this.id = id;
        }


        public List getEvents()
        {
            lastAccessTime = System.currentTimeMillis();
            return events;
        }


        public int getId()
        {
            return id;
        }


        public long getLastAccessTime()
        {
            return lastAccessTime;
        }


        public void treeNodesChanged(TreeModelEvent e)
        {
            events.addLast(new Event(EVENT_CHANGED, e));
        }


        public void treeNodesInserted(TreeModelEvent e)
        {
            events.addLast(new Event(EVENT_INSERTED, e));
        }


        public void treeNodesRemoved(TreeModelEvent e)
        {
            events.addLast(new Event(EVENT_REMOVED, e));
        }


        public void treeStructureChanged(TreeModelEvent e)
        {
            events.addLast(new Event(EVENT_STRUCTURE_CHANGED, e));
        }
    }


    private static class Event
    {

        private int kind;

        private TreeModelEvent event;


        public Event(int kind, TreeModelEvent event)
        {
            this.kind = kind;
            this.event = event;
        }


        public void process(HtmlTree tree)
        {
            switch (kind)
            {
                case EVENT_CHANGED:
                    tree.treeNodesChanged(event);
                    break;
                case EVENT_INSERTED:
                    tree.treeNodesInserted(event);
                    break;
                case EVENT_REMOVED:
                    tree.treeNodesRemoved(event);
                    break;
                case EVENT_STRUCTURE_CHANGED:
                    tree.treeStructureChanged(event);
                    break;
            }
        }
    }
}