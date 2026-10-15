package dev.dontpunchmytacz;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import punchy.config.PunchyConfig;

/** Item coverage adapted from lloy9d/dont-punch-my-tacz 0.5.2, GPL-3.0-only. */
final class BlacklistForce {

    private static final List<String> REMOVE = List.of(
            "create:extendo_grip",
            "tacz:.*",
            "superbwarfare:.*",
            "lrtactical:.*",
            "tacz:ammo",
            "tacz:attachment",
            "tacz:ammo_box",
            "tacz:gun_smith_table",
            "tacz:workbench_a",
            "tacz:workbench_b",
            "tacz:workbench_c",
            "tacz:target",
            "tacz:target_minecart",
            "tacz:statue"
    );

    private static final List<String> TACZ_IDS = List.of(
            "tacz:modern_kinetic_gun",
            "powergrid:portable_saw",
            "powergrid:portable_drill",
            "powergrid:electrozapper",
            "create:handheld_worldshaper",
            "create:potato_cannon",
            "simulated:plunger_launcher",
            "tacz:aa12",
            "tacz:ai_awp",
            "tacz:ak47",
            "tacz:aug",
            "tacz:b93r",
            "tacz:cz75",
            "tacz:db_long",
            "tacz:db_short",
            "tacz:deagle",
            "tacz:deagle_golden",
            "tacz:fn_evolys",
            "tacz:fn_fal",
            "tacz:g36k",
            "tacz:glock_17",
            "tacz:hk416d",
            "tacz:hk_g3",
            "tacz:hk_mk23",
            "tacz:hk_mp5a5",
            "tacz:kar98",
            "tacz:lonetrail",
            "tacz:m1014",
            "tacz:m107",
            "tacz:m16a1",
            "tacz:m16a4",
            "tacz:m1911",
            "tacz:m249",
            "tacz:m320",
            "tacz:m4a1",
            "tacz:m700",
            "tacz:m870",
            "tacz:m95",
            "tacz:m9a4",
            "tacz:minigun",
            "tacz:mk14",
            "tacz:p320",
            "tacz:p90",
            "tacz:qbz_191",
            "tacz:qbz_95",
            "tacz:rhino357",
            "tacz:rpg7",
            "tacz:rpk",
            "tacz:scar_h",
            "tacz:scar_l",
            "tacz:sks_tactical",
            "tacz:spas_12",
            "tacz:spr15hb",
            "tacz:springfield1873",
            "tacz:taurus500",
            "tacz:taurus943",
            "tacz:timeless50",
            "tacz:type_81",
            "tacz:ump45",
            "tacz:uzi",
            "tacz:vector45"
    );

    private static final List<String> SUPERB_WARFARE_IDS = List.of(
            "superbwarfare:glock_17",
            "superbwarfare:glock_18",
            "superbwarfare:mp_443",
            "superbwarfare:m_1911",
            "superbwarfare:trachelium",
            "superbwarfare:mp_5",
            "superbwarfare:vector",
            "superbwarfare:ak_47",
            "superbwarfare:ak_12",
            "superbwarfare:sks",
            "superbwarfare:m_4",
            "superbwarfare:hk_416",
            "superbwarfare:qbz_95",
            "superbwarfare:qbz_191",
            "superbwarfare:insidious",
            "superbwarfare:mk_14",
            "superbwarfare:ql_1031",
            "superbwarfare:marlin",
            "superbwarfare:k_98",
            "superbwarfare:mosin_nagant",
            "superbwarfare:svd",
            "superbwarfare:awm",
            "superbwarfare:m_98b",
            "superbwarfare:sentinel",
            "superbwarfare:hunting_rifle",
            "superbwarfare:ntw_20",
            "superbwarfare:homemade_shotgun",
            "superbwarfare:m_870",
            "superbwarfare:aa_12",
            "superbwarfare:devotion",
            "superbwarfare:rpk",
            "superbwarfare:m_60",
            "superbwarfare:m_2_hb",
            "superbwarfare:minigun",
            "superbwarfare:m_79",
            "superbwarfare:secondary_cataclysm",
            "superbwarfare:rpg",
            "superbwarfare:javelin",
            "superbwarfare:igla_9k38",
            "superbwarfare:bocek",
            "superbwarfare:super_star_shooter",
            "superbwarfare:nail_gun",
            "superbwarfare:repair_tool",
            "superbwarfare:taser",
            "superbwarfare:reforging",
            "superbwarfare:beast_gun_test",
            "superbwarfare:hand_grenade",
            "superbwarfare:rgo_grenade",
            "superbwarfare:m18_smoke_grenade",
            "superbwarfare:lunge_mine",
            "superbwarfare:skin_spray",
            "superbwarfare:hammer",
            "superbwarfare:golden_hammer",
            "superbwarfare:steel_hammer",
            "superbwarfare:diamond_hammer",
            "superbwarfare:cemented_carbide_hammer",
            "superbwarfare:netherite_hammer"
    );
    
    private static final List<String> LES_RAISINS_IDS = List.of(
            "lrtactical:melee",
            "lrtactical:throwable",
            "lrtactical:consumable",
            "lrtactical:flash_shield"
    );

    private static final List<String> CREATE_GUNSMITHING_IDS = List.of(
            "cgs:flintlock",
            "cgs:revolver",
            "cgs:shotgun",
            "cgs:nailgun",
            "cgs:gatling",
            "cgs:blazegun",
            "cgs:launcher",
            "cgs:hammer",
            "cgs:frag_grenade"
    );

    private static final List<String> MISC_IDS = List.of(
            "createdieselgenerators:chemical_sprayer",
            "createdieselgenerators:chemical_sprayer_lighter",
            "exposure:camera"
    );

    private static final List<String> VANILLA_IDS = List.of(
            "minecraft:brush"
    );

    private static final List<String> IDS = Stream.of(TACZ_IDS, SUPERB_WARFARE_IDS, LES_RAISINS_IDS,
                    CREATE_GUNSMITHING_IDS, MISC_IDS, VANILLA_IDS)
            .flatMap(Collection::stream)
            .toList();

    static void apply() {
        PunchyConfig.Data data = PunchyConfig.snapshot();
        Set<String> items = new LinkedHashSet<>(data.itemBlacklist);
        boolean changed = items.removeAll(REMOVE);
        changed |= items.addAll(IDS);
        for (String id : REMOVE) changed |= data.blacklistApplyDualHanded.remove(id) != null;
        for (String id : IDS) {
            if (!data.blacklistApplyDualHanded.containsKey(id)) {
                data.blacklistApplyDualHanded.put(id, false);
                changed = true;
            }
        }
        if (changed) {
            data.itemBlacklist = List.copyOf(items);
            PunchyConfig.applyAndSave(data);
            DontPunchMyTacz.LOGGER.info("Updated Punchy compatibility item exclusions");
        }
    }
}
