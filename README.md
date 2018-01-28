# ch.vorburger.minecraft.osgi

This is a [spongepowered.org](https://www.spongepowered.org/) Minecraft Server plugin which embeds an OSGi Framework ([Apache Felix](http://felix.apache.org/)).

This Minecraft plugin can then (dynamically, HOT, re-*) load OSGi bundles as additional Minecraft plugins.  This dynamicity is very useful particularly during development of extensions to a game which has a relatively "heavy" start-up time, incl. the typical disconnecting of game clients, and thus gives you a very quick in-game change turn around experience while coding.

[Watch this early POC video on YouTube demonstrating live Minecraft mod development](https://www.youtube.com/watch?v=mibW8MhenGc), and see the [vorburger/HoTea project](https://github.com/vorburger/HoTea) for an earlier pre-OSGi take on (and superseded by) this.  Also check out the [ch.vorburger.osgi.gradle companion project](https://github.com/vorburger/ch.vorburger.osgi.gradle).

Licensed under the [GNU Affero General Public License v3.0 (AGPLv3)](LICENSE).  Contributions most welcome.

Please Star & Watch this project if it's of any interest or use to you!

## How to use this

### Build it

    git clone https://github.com/vorburger/ch.vorburger.minecraft.osgi/
    cd ch.vorburger.minecraft.osgi
    git submodule update --init --recursive
    cd ch.vorburger.osgi.gradle
    ./gradlew install test
    cd ..
    ./gradlew install test

NB: You must use `install` and not just `build` _(because the MinecraftOSGiTest fails otherwise)._

## Get a Sponge powered Minecraft server

Create a home directory for the Minecraft server, and download the matching version (see `ch.vorburger.minecraft.osgi/build.gradle`) of the Sponge Vanilla JAR from [spongepowered.org](https://www.spongepowered.org) (e.g. spongevanilla-1.10.2-5.1.0-BETA-374.jar) into it, and try starting it once:

    cd ..
    mkdir spongevanilla-1.10.2-5.1.0-BETA-374
    cd spongevanilla-1.10.2-5.1.0-BETA-374
    wget https://repo.spongepowered.org/maven/org/spongepowered/spongevanilla/1.10.2-5.1.0-BETA-374/spongevanilla-1.10.2-5.1.0-BETA-374.jar
    echo "eula=true" >eula.txt
    java -jar spongevanilla-*.jar

## Install minecraft.osgi & its dependencies

We now copy the Sponge OSGi mod we just built from source above into the `mods/` directory of our Sponge Vanilla server we just set up:

    cp ../ch.vorburger.minecraft.osgi/ch.vorburger.minecraft.osgi/build/libs/*.jar mods/

This mod embeds an OSGi "kernel" (framework), and you could now start your Sponge server with this mod alone - this will work, but it's not particularly interesting; it won't "do" anything, yet.

But we can now install a few OSGi mod bundles built earlier; those JARs need to be put under a (new) `osgi/boot` directory (NOT `mods/`), like so:

    mkdir -vp osgi/boot/

    cp ../ch.vorburger.minecraft.osgi/ch.vorburger.osgi.gradle/org.gradle.tooling.osgi/build/libs/*.jar osgi/boot/4_org.gradle.tooling.osgi-3.3.jar
    cp ../ch.vorburger.minecraft.osgi/ch.vorburger.osgi.gradle/ch.vorburger.osgi.gradle/build/libs/*.jar osgi/boot/4_ch.vorburger.osgi.gradle-1.0.0-SNAPSHOT.jar
    cp ../ch.vorburger.minecraft.osgi/ch.vorburger.minecraft.osgi.templates/build/libs/*.jar osgi/boot/5_ch.vorburger.minecraft.osgi.templates-1.0.0-SNAPSHOT.jar
    cp ../ch.vorburger.minecraft.osgi/ch.vorburger.minecraft.osgi.dev/build/libs/*.jar osgi/boot/5_ch.vorburger.minecraft.osgi.dev-1.0.0-SNAPSHOT.jar
    cp ../ch.vorburger.minecraft.osgi/ch.vorburger.minecraft.osgi.users/build/libs/*.jar osgi/boot/6_ch.vorburger.minecraft.osgi.users-1.0.0-SNAPSHOT.jar

Some of these bundles above have dependencies to some external 3rd party libraries, which are all valid OSGi bundles themselves, that must now ALL (!) also be installed into `osgi/boot`:

    wget -N http://search.maven.org/remotecontent?filepath=com/google/guava/guava/18.0/guava-18.0.jar -O osgi/boot/3_guava-18.0.jar
    wget -N http://search.maven.org/remotecontent?filepath=com/google/guava/guava/19.0/guava-19.0.jar -O osgi/boot/3_guava-19.0.jar
    wget -N http://search.maven.org/remotecontent?filepath=com/google/guava/guava/20.0/guava-20.0.jar -O osgi/boot/3_guava-20.0.jar
    wget -N http://search.maven.org/remotecontent?filepath=org/apache/felix/org.apache.felix.log/1.0.1/org.apache.felix.log-1.0.1.jar -O osgi/boot/1_org.apache.felix.log-1.0.1.jar
    wget -N http://search.maven.org/remotecontent?filepath=org/everit/osgi/org.everit.osgi.loglistener.slf4j/1.0.0/org.everit.osgi.loglistener.slf4j-1.0.0.jar -O osgi/boot/2_org.everit.osgi.loglistener.slf4j-1.0.0.jar
    wget -N http://search.maven.org/remotecontent?filepath=org/eclipse/xtext/org.eclipse.xtext.xbase.lib/2.10.0/org.eclipse.xtext.xbase.lib-2.10.0.jar -O osgi/boot/3_org.eclipse.xtext.xbase.lib-2.10.0.jar
    wget -N http://search.maven.org/remotecontent?filepath=org/eclipse/xtend/org.eclipse.xtend.lib.macro/2.10.0/org.eclipse.xtend.lib.macro-2.10.0.jar -O osgi/boot/3_org.eclipse.xtend.lib.macro-2.10.0.jar
    wget -N http://search.maven.org/remotecontent?filepath=org/eclipse/xtend/org.eclipse.xtend.lib/2.10.0/org.eclipse.xtend.lib-2.10.0.jar -O osgi/boot/3_org.eclipse.xtend.lib-2.10.0.jar

Note how, thanks to OSGi, we have no "classpath hell" issues - we can, easily, have several different versions of the same library, such as Guava!

Later, you can also add any other OSGi bundles into the `osgi/` directory.  (Note that the numeric prefix indicates the order in which the bundles are installed and started.)

## Developing OSGi bundled Minecraft mods

The `minecraft.osgi.users` which we just installed above makes it easy to now develop your own OSGi bundled Minecraft mods:

1. Create a directory, where your own mods will go:

    mkdir -vp dev/

2. Start the Sponge Vanilla server create above, and verify that there are no errors shown in the log on the console:

    java -jar spongevanilla-*.jar

3. Connect your Minecraft client to this local server

4. In game, you should have just been greeted with a `HELO` message followed by `.. will install ..`.  This chat message is issued by `minecraft.osgi.users` on join.  More importantly, it has created a new development project for you under the `dev/` directory (which you created above).  The project is under a sub-directory with your player's Minecraft UUID.

5. Open this new dev project in your favourite Java IDE (like e.g. [Eclipse](https://eclipse.org/downloads/), or IntelliJ's IDEA).  Note that this project uses Gradle for dependency management, so you must have Gradle support in your IDE (e.g. the latest Eclipse version Oxygen already includes [Buildship](https://projects.eclipse.org/projects/tools.buildship)).  (Gradle is also used to produced a valid OSGi MANIFEST.MF for the JAR, via [BND](http://bnd.bndtools.org).)

6. Try out the example command this project registered by typing `/hello` into the in-game chat window (it should reply with the obligatory `Hello World!`).  Likewise, if you bump into anything, it will say `boing` in the chat.

7. Your mod can use the full [Sponge API](https://jd.spongepowered.org/).  But note how this OSGi-based Minecraft Mod uses an OSGi Bundle `Activator` (or BP or DS), instead of the typical Sponge `@Plugin` annotated class, to register its Minecraft Commands and Event Listeners. (_BTW: You could, of course, make your own Mod have both a Sponge Plugin class as well as an OSGi Activator, and register the same commands and listeners in it; thus creating a JAR which could be used in both environments._)

8. Try changing something, say fix the chat message printed the `HelloWorldCommand` class from the (wrong) `"Hello World!"` to the ([correct](https://en.wikipedia.org/wiki/%22Hello,_World!%22_program#History)) `"hello, world"` ... ;-)

9. Now note how, as soon as you save this changed Java source file (even if your IDE were to not have any build), a Gradle running in continous mode, launched in the Minecraft server, by the `minecraft.osgi.dev` plugin, will detect the change, execute a re-build, which will re-create the bundle JAR, which will get (HOT) re-loaded into OSGi!  Do check out the Sponge Vanilla server console to see the log messages piped through from Gradle, and the OSGi reload notifications.

10. If you re-try typing `/hello` now, you'll get the changed message.  This will work for ANY code changes, incl. new classes, etc.

## Reference doc

The `/osgi:install <URI>` command, where _URI_ is typically a `file:/` prefixed path to an OSGi bundle JAR file, or a directory to a Gradle project, installs (and, if it's a directory, continuously builds and HOT reloads) that OSGi bundle.  This lets you work with projects outside of your `dev/<User-UUID>/project1` directory.

The `osgi/installedBundles` file lists all so installed bundles.  What is listed here is started on server boot, after those in the `osgi/system` directory.  You can edit this file, and e.g. remove the example `dev/<User-UUID>/project1` from here.

When using it like this, you do not need to copy the `osgi.templates` and `osgi.users` (but all the other ones listed above).

## What else?

The `ch.vorburger.minecraft.news` and `ch.vorburger.minecraft.worlds` are two example OSGi mods which you don't need to write your own, but can have a look at, and could also optionally install into `osgi/boot` (with boot order prefix `6_...`) if you like.
