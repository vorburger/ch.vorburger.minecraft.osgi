/**
 * ch.vorburger.minecraft.osgi
 *
 * Copyright (C) 2016 - 2017 Michael Vorburger.ch <mike@vorburger.ch>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.vorburger.minecraft.testutils;

import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.lwts.AbstractTestTweaker;

public class TestTweaker extends AbstractTestTweaker {

    // https://github.com/SpongePowered/LaunchWrapperTestSuite

    @Override
    public void injectIntoClassLoader(LaunchClassLoader loader) {
        super.injectIntoClassLoader(loader);

        registerAccessTransformer("META-INF/common_at.cfg");
        registerAccessTransformer("META-INF/vanilla_at.cfg");

        MixinBootstrap.init();
        // TODO auto. ClassLoader getResources enumerate all mixins.*.json instead manually listing:
        Mixins.addConfiguration("mixins.common.api.json");
        Mixins.addConfiguration("mixins.common.blockcapturing.json");
        Mixins.addConfiguration("mixins.common.bungeecord.json");
        Mixins.addConfiguration("mixins.common.concurrentchecks.json");
        Mixins.addConfiguration("mixins.common.core.json");
        Mixins.addConfiguration("mixins.common.entityactivation.json");
        Mixins.addConfiguration("mixins.common.entitycollisions.json");
        Mixins.addConfiguration("mixins.common.exploit.json");
        Mixins.addConfiguration("mixins.common.multi-world-command.json");
        Mixins.addConfiguration("mixins.common.optimization.json");
        Mixins.addConfiguration("mixins.common.realtime.json");
        Mixins.addConfiguration("mixins.common.refmap.json");
        Mixins.addConfiguration("mixins.common.tracking.json");
        Mixins.addConfiguration("mixins.common.vanilla-command.json");
        Mixins.addConfiguration("mixins.vanilla.chunkio.json");
        Mixins.addConfiguration("mixins.vanilla.core.json");
        Mixins.addConfiguration("mixins.vanilla.entityactivation.json");
        Mixins.addConfiguration("mixins.vanilla.refmap.json");

        // Mixins.addConfiguration("mixins.common.refmap.json");
        // Set Mixin side, otherwise you get a warning when running the tests
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.SERVER);

        // loader.registerTransformer("com.example.test.launch.transformer.MyCustomTransformer")
    }

}
