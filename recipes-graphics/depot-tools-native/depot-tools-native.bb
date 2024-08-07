AUTHOR = "Sony Group Corporation"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c2c05f9bdd5fc0b458037c2d1fb8d95e"

SRC_URI = "git://chromium.googlesource.com/chromium/tools/depot_tools.git;protocol=https;branch=main"
SRCREV = "670ce748bea1fbb87cf707718239bc3a4c3a08ad"

S = "${WORKDIR}/git"

inherit native

do_compile() {
    # Run gclint once to download bootstrap
    cd ${S}
    export PATH=${S}:$PATH
    export DEPOT_TOOLS_UPDATE=0
    export GCLIENT_PY3=1
    gclient --version
}
# Enable network access from tasks
do_compile[network] = "1"

do_install() {
    install -d ${D}${datadir}/depot_tools
    cp -r ${S}/. ${D}${datadir}/depot_tools
}

FILES:${PN}-dev = "${datadir}/depot_tools/*"

INSANE_SKIP:${PN}-dev = "already-stripped"

BBCLASSEXTEND += "native"
