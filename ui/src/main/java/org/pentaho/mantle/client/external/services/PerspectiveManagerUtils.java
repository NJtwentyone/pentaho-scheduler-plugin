/*!
 *
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 *
 * Copyright (c) 2002-2023 Hitachi Vantara. All rights reserved.
 *
 */

package org.pentaho.mantle.client.external.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;
import org.pentaho.mantle.client.external.services.definitions.IPerspectiveManagerUtils;
import org.pentaho.mantle.client.workspace.SchedulesPerspectivePanel;

import java.util.Iterator;

public class PerspectiveManagerUtils implements IPerspectiveManagerUtils {

  static {
    setupNativeHooks( new PerspectiveManagerUtils() );
  }

  public void showSchedulesPerspective( DeckPanel contentDeck ) {

    GWT.runAsync( new RunAsyncCallback() {

      public void onSuccess() {
        console("PerspectiveManagerUtils#onSuccess: start !!!!!");
        console("contentDeck:" + contentDeck );
        try {

          if ( hasChildren( contentDeck ) &&
            contentDeck.getWidgetIndex( SchedulesPerspectivePanel.getInstance() ) == -1 ) {
            console("PerspectiveManagerUtils 9!!!!!");
            contentDeck.add( SchedulesPerspectivePanel.getInstance() );
          } else {
            console("PerspectiveManagerUtils 10!!!!!");
            SchedulesPerspectivePanel.getInstance().refresh();
          }
          console("PerspectiveManagerUtils 11!!!!!");
          contentDeck.showWidget( contentDeck.getWidgetIndex( SchedulesPerspectivePanel.getInstance() ) );
        } catch (  Exception e ){
          console("PerspectiveManagerUtils 7!!!!!", e);
//          SchedulesPerspectivePanel.getInstance().refresh();
//          contentDeck.add( SchedulesPerspectivePanel.getInstance()  );
//          contentDeck.showWidget( contentDeck.getWidgetIndex( SchedulesPerspectivePanel.getInstance() ) );
        }
        console("PerspectiveManagerUtils#onSuccess: end !!!!!");
      }

      public void onFailure( Throwable reason ) {
        console(" PerspectiveManagerUtils 1!!!!!", reason);
        SchedulesPerspectivePanel.getInstance().refresh();
      }
    } );
  }

  public static void console(String text, Throwable t) {
    console(text + " thowable-message:" + t.getMessage());
  }
  public static native void console(String text)
/*-{
    console.log(text);
}-*/;

  // DEBUG
  public boolean hasChildren( DeckPanel deckPanel ) {
    boolean hasChildren = false;
    try {
      hasChildren = deckPanel.getWidgetCount() > 0;
      console("PerspectiveManagerUtils#hasChildren true with deckPanel.getWidgetCount(): " + deckPanel.getWidgetCount() );
      Iterator<Widget> it = deckPanel.iterator();
      while(it.hasNext()) {
        Widget next = it.next();
        console( "\tPerspectiveManagerUtils#hasChildren true with deckPanel.iterator().next().getElement().getId(): \""
          + next.getElement().getId() + "\"" );
      }
    }
    catch (  Exception e ) {
      console("PerspectiveManagerUtils 2!!!!!", e );
    }
    return hasChildren;
  }

  private static native void setupNativeHooks( PerspectiveManagerUtils utils )
  /*-{
    $wnd.pho.showSchedulesPerspective = function(contentDeck) {
      //CHECKSTYLE IGNORE LineLength FOR NEXT 1 LINES
      return utils.@org.pentaho.mantle.client.external.services.PerspectiveManagerUtils::showSchedulesPerspective(Lcom/google/gwt/user/client/ui/DeckPanel;)(contentDeck);
    }
  }-*/;
}
