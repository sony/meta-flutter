AUTHOR = "Flutter authors"
HOMEPAGE = "https://github.com/flutter/engine"
BUGTRACKER = "https://github.com/flutter/flutter/issues"
SECTION = "graphics"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/src/flutter/LICENSE;md5=a60894397335535eb10b54e2fff9f265"

inherit pkgconfig

# TODO: Add dependent packages.
DEPENDS = "freetype \
           curl-native ca-certificates-native depot-tools-native \
           wayland wayland-protocols wayland-native cairo"

GN_TOOLS_PYTHON2_PATH ??= "bootstrap-2@3.8.10.chromium.23_bin/python/bin"

# Flutter config
require conf/flutter-engine.conf
PACKAGECONFIG[debug-mode] = "--runtime-mode debug --unoptimized"
PACKAGECONFIG[profile-mode] = "--runtime-mode profile --no-lto"
PACKAGECONFIG[release-mode] = "--runtime-mode release"

require gn-args-utils.inc
GN_TARGET_OS = "linux"
GN_TARGET_ARCH = "arm64"

GN_ARGS = "--target-sysroot ${STAGING_DIR_TARGET}${PACKAGECONFIG_CONFARGS}"
GN_ARGS:append = " --target-os ${GN_TARGET_OS}"
GN_ARGS:append = " --linux-cpu ${GN_TARGET_ARCH}"
GN_ARGS:append = " --arm-float-abi hard"
GN_ARGS:append = " --embedder-for-target"
GN_ARGS:append = " --disable-desktop-embeddings"
GN_ARGS:append = " --no-build-embedder-examples"
GN_ARGS:append = " --enable-fontconfig"
GN_ARGS:append = " --no-goma"

ARTIFACT_DIR = "${@get_engine_artifact_dir(d)}"

do_populate_lic[depends] += "${PN}:do_configure"

do_configure() {
    export DEPOT_TOOLS=${STAGING_DIR_NATIVE}/${datadir}/depot_tools
    export PATH=${DEPOT_TOOLS}:${DEPOT_TOOLS}/${GN_TOOLS_PYTHON2_PATH}:$PATH
    export DEPOT_TOOLS_UPDATE=0
    export CURL_CA_BUNDLE=${STAGING_DIR_NATIVE}/etc/ssl/certs/ca-certificates.crt

    cd ${WORKDIR}
    echo 'solutions = [
        {
            "managed" : False,
            "name" : "src/flutter",
            "url" : "https://github.com/flutter/engine.git@${ENGINE_VERSION}",
            "custom_deps": {},
            "deps_file": "DEPS",
            "safesync_url": "",
            "custom_vars": {
                "download_android_deps": False,
                "download_windows_deps": False,
            },
        },
    ]' > .gclient
    gclient sync

    cd ${WORKDIR}/src
    ./flutter/tools/gn ${GN_ARGS}
}
do_configure[depends] += "depot-tools-native:do_populate_sysroot"
do_configure[network] = "1"

do_compile() {
    export DEPOT_TOOLS=${STAGING_DIR_NATIVE}/${datadir}/depot_tools
    export PATH=${DEPOT_TOOLS}:${DEPOT_TOOLS}/${GN_TOOLS_PYTHON2_PATH}:$PATH
    export DEPOT_TOOLS_UPDATE=0

    cd ${WORKDIR}/src
    ninja -C ${ARTIFACT_DIR}
}

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${WORKDIR}/src/${ARTIFACT_DIR}/libflutter_engine.so ${D}${libdir}
}

FILES:${PN} = "${libdir}"
FILES:${PN}-dev = "${includedir}"

INSANE_SKIP:${PN}:append = "already-stripped"
