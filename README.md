# ENG1 Goose Game

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

## Development Standards
- All code should follow the [Google style guide](https://google.github.io/styleguide/javaguide.html).
- Javadoc should be in traditional style.
- Comments should be in sentence case, with correct grammar and with a single space after the `//`.
- Always commit LF line endings, not CRLF.

## Licences and credits

### Third-party libraries
The runnable jar built by `lwjgl3:jar` bundles the libraries below. Each is used unmodified, under its own licence.

| Library | Version | Licence |
|---|---|---|
| [libGDX](https://github.com/libgdx/libgdx) (`gdx`, `gdx-backend-lwjgl3`, `gdx-platform`, `gdx-jnigen-loader`) | 1.14.2 | [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [LWJGL](https://github.com/LWJGL/lwjgl3) (`lwjgl`, `lwjgl-glfw`, `lwjgl-jemalloc`, `lwjgl-openal`, `lwjgl-opengl`, `lwjgl-stb`) | 3.4.3 | BSD 3-Clause (full text below) |
| [GLFW](https://www.glfw.org/), bundled native library of LWJGL | | [zlib/libpng License](https://www.glfw.org/license.html) |
| [OpenAL Soft](https://github.com/kcat/openal-soft), bundled native library of LWJGL | | [GNU LGPL v2](https://www.gnu.org/licenses/old-licenses/lgpl-2.0.html); source code available at https://github.com/kcat/openal-soft |
| [jemalloc](https://github.com/jemalloc/jemalloc), bundled native library of LWJGL | | [BSD 2-Clause](https://github.com/jemalloc/jemalloc/blob/dev/COPYING) |
| [stb](https://github.com/nothings/stb), bundled native library of LWJGL | | Public domain (alternatively MIT) |
| [JLayer](http://www.javazoom.net/javalayer/javalayer.html) (libGDX build, `jlayer-1.0.1-gdx`) | 1.0.1 | [GNU LGPL](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html); source code available at http://www.javazoom.net/javalayer/sources.html |
| [JOrbis](http://www.jcraft.com/jorbis/) | 0.0.17 | [GNU LGPL](https://www.gnu.org/licenses/old-licenses/lgpl-2.0.html); source code available at http://www.jcraft.com/jorbis/ |
| Liberation Sans (libGDX's built-in `lsans-15` font, used by every default `BitmapFont`) | | [SIL Open Font License 1.1](https://openfontlicense.org/open-font-license-official-text/) |
| [Gradle wrapper](https://github.com/gradle/gradle) (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`) | 9.8.0 | [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| `StartupHelper.java` in `lwjgl3`, from the [gdx-liftoff](https://github.com/libgdx/gdx-liftoff) project template (fixes JVM start-up on macOS and Linux; its licence header is kept in the file) | | [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) |

libGDX and Gradle do not publish a NOTICE file, so there is no NOTICE text to reproduce.

### Assets
| File | Source | Licence |
|---|---|---|
| `assets/yorkshirepudding.jpg` | "Yorkshire Pudding" by stef yau, via [Wikimedia Commons](https://commons.wikimedia.org/wiki/File:Yorkshire_Pudding.jpg) (originally posted to [Flickr](https://www.flickr.com/photos/49502976207@N01/77683987)), cropped to 1:1. Wikimedia Commons confirmed the CC BY 2.0 licence on 13 August 2007; the photographer has since stopped offering it under that licence on Flickr, but a Creative Commons licence cannot be revoked for copies already released under it. | [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/) |
| `assets/AirhornSound.wav` | "Air Horn Sound Effects" by DaniTheSoundUser, [Freesound](https://freesound.org/s/587237/), converted to WAV | [CC0 1.0](https://creativecommons.org/publicdomain/zero/1.0/) |
| `assets/goosedummy.png`, `assets/Goosedisguise.png`, `assets/long_boi.png`, `assets/fox.png`, `assets/playerdummy.png`, `assets/Airhorn.png` | Pixel art created for this project with AI assistance (Claude, by Anthropic): Claude wrote a Python script using the [Pillow](https://python-pillow.org/) library that draws each sprite | Made for this project; no third-party licence applies |
| `assets/park-tiles.png`, `assets/park-tiles.tsx`, `assets/park.tmx` | Tileset and park map created for this project with AI assistance (Claude, by Anthropic): the tileset was drawn by a Python/Pillow script, and the map was generated by a script and painted with the [Tiled Map Editor](https://www.mapeditor.org/) 1.12.2 | Made for this project; no third-party licence applies |
| `lwjgl3/src/main/resources/libgdx16.png`, `libgdx32.png`, `libgdx64.png`, `libgdx128.png`, `lwjgl3/icons/logo.png`, `logo.ico`, `logo.icns` | Window and app icons from the [gdx-liftoff](https://github.com/libgdx/gdx-liftoff) project template | [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) |

The AI-generated assets were made with Claude (Opus 5.5) in November 2026, whose [terms of service](https://www.anthropic.com/legal/consumer-terms) permit this use.
