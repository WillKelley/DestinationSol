/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.destinationsol.game.screens;

import com.badlogic.gdx.math.Vector2;
import org.destinationsol.GameOptions;
import org.destinationsol.SolApplication;
import org.destinationsol.game.SolGame;
import org.destinationsol.game.gun.GunItem;
import org.destinationsol.game.item.EngineItem;
import org.destinationsol.game.item.ItemContainer;
import org.destinationsol.game.item.ShipItem;
import org.destinationsol.game.item.SolItem;
import org.destinationsol.game.ship.ShipRepairer;
import org.destinationsol.game.ship.SolShip;
import org.destinationsol.game.ship.hulls.Hull;
import org.destinationsol.game.ship.hulls.HullConfig;
import org.destinationsol.ui.SolInputManager;
import org.destinationsol.ui.SolUiControl;
import org.destinationsol.ui.UiDrawer;

import java.util.ArrayList;
import java.util.List;

public class ChangeShip implements InventoryOperations {

  private final ArrayList<SolUiControl> myControls;
  private final SolUiControl myBuyCtrl;

  public ChangeShip(InventoryScreen inventoryScreen, GameOptions gameOptions) {
    myControls = new ArrayList<SolUiControl>();

    myBuyCtrl = new SolUiControl(inventoryScreen.itemCtrl(0), true, gameOptions.getKeyChangeShip());
    myBuyCtrl.setDisplayName("Change");
    myControls.add(myBuyCtrl);
  }

  @Override
  public ItemContainer getItems(SolGame game) {
    return game.getScreens().talkScreen.getTarget().getTradeContainer().getShips();
  }

  @Override
  public boolean isUsing(SolGame game, SolItem item) {
    return false;
  }

  @Override
  public float getPriceMul() {
    return 1;
  }

  @Override
  public String getHeader() {
    return "Ships:";
  }

  @Override
  public List<SolUiControl> getControls() {
    return myControls;
  }

  @Override
  public void updateCustom(SolApplication cmp, SolInputManager.Ptr[] ptrs, boolean clickedOutside) {
    SolGame game = cmp.getGame();
    InventoryScreen is = game.getScreens().inventoryScreen;
    SolShip hero = game.getHero();
    TalkScreen talkScreen = game.getScreens().talkScreen;
    if (talkScreen.isTargetFar(hero)) {
      cmp.getInputMan().setScreen(cmp, game.getScreens().mainScreen);
      return;
    }
    SolItem selItem = is.getSelectedItem();
    if(selItem == null) {
      myBuyCtrl.setDisplayName("---");
      myBuyCtrl.setEnabled(false);
      return;
    }
    boolean enabled = hasMoneyToBuyShip(hero, selItem);
    boolean sameShip = isSameShip(hero, selItem);
    if(enabled && !sameShip) {
      myBuyCtrl.setDisplayName("Change");
      myBuyCtrl.setEnabled(true);
    } else if(enabled && sameShip) {
      myBuyCtrl.setDisplayName("Have it");
      myBuyCtrl.setEnabled(false);
      return;
    } else {
      myBuyCtrl.setDisplayName("---");
      myBuyCtrl.setEnabled(false);
      return;
    }
    if (myBuyCtrl.isJustOff()) {
      hero.setMoney(hero.getMoney() - selItem.getPrice());
      changeShip(game, hero, (ShipItem) selItem);
    }
  }

  private boolean hasMoneyToBuyShip(SolShip hero, SolItem shipToBuy) {
    return hero.getMoney() >= shipToBuy.getPrice();
  }

  private boolean isSameShip(SolShip hero, SolItem shipToBuy) {
    if(shipToBuy instanceof ShipItem) {
      ShipItem ship = (ShipItem) shipToBuy;
      HullConfig config1 = ship.getConfig();
      HullConfig config2 = hero.getHull().getHullConfig();
      return config1.equals(config2);
    } else {
      throw new IllegalArgumentException("ChangeShip:isSameShip received " + shipToBuy.getClass() + " argument instead of ShipItem!");
    }
  }

  private void changeShip(SolGame game, SolShip hero, ShipItem selected) {
    HullConfig newConfig = selected.getConfig();
    Hull hull = hero.getHull();
    EngineItem.Config ec = newConfig.getEngineConfig();
    EngineItem ei = ec == null ? null : ec.example.copy();
    GunItem g2 = hull.getGun(true);
    SolShip newHero = game.getShipBuilder().build(game, hero.getPosition(), new Vector2(), hero.getAngle(), 0, hero.getPilot(),
      hero.getItemContainer(), newConfig, newConfig.getMaxLife(), hull.getGun(false), g2, null,
      ei, new ShipRepairer(), hero.getMoney(), hero.getTradeContainer(), hero.getShield(), hero.getArmor());
    game.getObjMan().removeObjDelayed(hero);
    game.getObjMan().addObjDelayed(newHero);
  }

  @Override
  public boolean isCursorOnBg(SolInputManager.Ptr ptr) {
    return false;
  }

  @Override
  public void onAdd(SolApplication cmp) {

  }

  @Override
  public void blurCustom(SolApplication cmp) {

  }

  @Override
  public void drawBg(UiDrawer uiDrawer, SolApplication cmp) {

  }

  @Override
  public void drawImgs(UiDrawer uiDrawer, SolApplication cmp) {

  }

  @Override
  public void drawText(UiDrawer uiDrawer, SolApplication cmp) {

  }

  @Override
  public boolean reactsToClickOutside() {
    return false;
  }
}
