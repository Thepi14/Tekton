package tekton;

import arc.Core;
import arc.func.Boolc;
import arc.func.Prov;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import mindustry.content.TechTree;
import mindustry.gen.Icon;
import mindustry.type.Planet;
import mindustry.ui.dialogs.SettingsMenuDialog;
import tekton.content.TektonPlanets;
import tekton.ui.*;

import static arc.Core.*;
import static mindustry.Vars.*;
import static tekton.Tekton.ID;

public class TektonSettings {
	private static SettingsMenuDialog.SettingsTable table;
    
    public static void load() {
        ui.settings.addCategory(bundle.get("setting.tekton-title"), "tekton-icon-style", t -> {
            table = t;

            //t.pref(new BannerPref(ID + "-modname", 256));

            separator("tekton-data");

            buttonPref("tekton-clear-campaign", Icon.save,() -> {
                ui.showConfirm("@confirm", "@settings.tekton-clear-campaign.confirm", () -> {
                    resetSaves(TektonPlanets.tekton);
                    ui.showInfoOnHidden("@settings.tekton-clear-campaign-close.confirm", () -> {
                        Core.app.exit();
                    });
                });
            });
            buttonPref("tekton-clear-tech-tree", Icon.tree,() -> {
                ui.showConfirm("@confirm", "@settings.tekton-clear-tech-tree.confirm", () -> resetTree(TektonPlanets.tekton.techTree));
            });
        });
    }

    public static boolean drawerMode() {return boolDef("drawer-mode",true);}
    public static boolean wallTiling() {
        return boolDef("wall-tiling", true);
    }
    public static boolean autoUpdate() {
        return boolDef("autoupdate", true);
    }
    public static boolean unitUwu() {
        return boolDef("leeft-uwu", false);
    }

    public static boolean bool(String key) {
        return settings.getBool("tekton-" + key);
    }
    public static boolean boolDef(String key, boolean def) {
        return settings.getBool("tekton-" + key, def);
    }
    public static void bool(String key, boolean bool) {
        settings.put("tekton-" + key, bool);
    }
    public static int i(String key) {
        return settings.getInt("tekton-" + key);
    }
    public static int iDef(String key, int def) {
        return settings.getInt("tekton-" + key, def);
    }
    public static void i(String key, int val) {
        settings.put("tekton-" + key, val);
    }

    public static void resetSaves(Planet planet) {
        planet.sectors.each(sector -> {
            if (sector.hasSave()) sector.save.delete();
        });
    }
    public static void resetTree(TechTree.TechNode root) {
        root.reset();
        root.content.clearUnlock();
        root.children.each(TektonSettings::resetTree);
    }

    static void separator(String name) {
        table.pref(new SeparatorPref(name));
    }
    static void buttonPref(String name, Drawable drawable, Runnable listener) {
        table.pref(new ButtonPref(name, drawable, listener));
    }
    static void buttonPref(String name, Prov<CharSequence> title, Drawable drawable, Runnable listener) {
        table.pref(new ButtonPref(name, drawable, title, listener));
    }
    static void sliderPref(String ico, String name, int def, int min, int max, SettingsMenuDialog.StringProcessor p) {
        table.pref(new SliderIconSetting(ico,name, def,min,max,1,p));
        settings.defaults(name, def);
    }
    static void switchPref(String ico, String name, boolean def) {
        table.pref(new SwitchPref(ico,name,def,null));
        settings.defaults(name, def);
    }
    static void switchPref(String ico, String name, boolean def, Boolc changed) {
        table.pref(new SwitchPref(ico,name,def,changed));
        settings.defaults(name, def);
    }
    static void checkPref(String ico, String name, boolean def) {
        table.pref(new CheckIconSetting(ico,name,def,null));
        settings.defaults(name, def);
    }
    static void checkPref(String ico, String name, boolean def, Boolc changed) {
        table.pref(new CheckIconSetting(ico,name,def,changed));
        settings.defaults(name, def);
    }
}