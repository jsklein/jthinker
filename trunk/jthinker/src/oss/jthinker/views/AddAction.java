/*
 * Copyright (c) 2008, Ivan Appel <ivan.appel@gmail.com>
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. 
 * 
 * Neither the name of Ivan Appel nor the names of any other jThinker
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package oss.jthinker.views;

import oss.jthinker.widgets.JXPopupMenu;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import oss.jthinker.diagrams.NodeSpecHolder;
import oss.jthinker.tocmodel.NodeType;
import oss.jthinker.widgets.JNode;
import oss.jthinker.widgets.JNodeHost;
import oss.jthinker.widgets.JNodeSpec;
import static oss.jthinker.tocmodel.NodeType.*;
import static oss.jthinker.widgets.BorderType.*;

/**
 * Action for adding node to {@link DiagramPane}.
 * 
 * @author iappel
 */
public class AddAction extends AbstractAction {
    private final NodeType nodeKind;
    private final DiagramPane linkPane;
    private final JXPopupMenu popupMenu;
    
    /**
     * Creates a new AddAction instance.
     * 
     * @param pane diagram view, where this action will happen
     * @param menu menu to fetch coordinates from
     * @param kind kind of the node to be created with this action
     */
    public AddAction(DiagramPane pane, JXPopupMenu menu, NodeType kind) {
        super(nodeKindToString(kind));
        nodeKind = kind;
        linkPane = pane;
        popupMenu = menu;
    }

    /**
     * Converts {@link NodeType} into string of menuitem's title.
     * 
     * @param kind kind of the node
     * @return appropriate addition menuitem's title
     */
    public static String nodeKindToString(NodeType kind) {
        switch (kind) {
            case STATEMENT:
                return "Add statement";
            case ELLIPSE:
                return "Add ellipse";
            case TASK:
                return "Add task";
            case OBSTACLE:
                return "Add obstacle";
            default:
                throw new IllegalArgumentException("Illegal nodeKind = " + kind);
        }
    }

    /** {@inheritDoc} */
    public void actionPerformed(ActionEvent e) {
        JNode node = createNode(linkPane.getLinkController(),
                nodeKind, popupMenu.getLastDisplayLocation());
        if (node == null) {
            return;
        }
        linkPane.getLinkController().add(node);
    }
    
    /**
     * Creates a node of given type that should be managed by given
     * node host
     * @param parent host to manage the node
     * @param nodeType type of the node
     * @param center center point for new node
     * @return a new node of given parametres
     */
    public static JNode createNode(JNodeHost parent, NodeType nodeType, Point center) {
        JNodeSpec protoSpec = NodeSpecHolder.getSpec(nodeType);
        String s = protoSpec.isEditable() ?
            JOptionPane.showInputDialog("What to add?") : "          ";
        if (s == null) {
            return null;
        }
        JNodeSpec result = protoSpec.clone(s, center);
        return new JNode(parent, result);
    }
}