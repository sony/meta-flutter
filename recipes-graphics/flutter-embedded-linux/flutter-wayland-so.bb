SUMMARY = "Flutter embedding for eLinux with Wayland backend (.so)"
DESCRIPTION = "Build .so file of the Flutter embedding for eLinux with Wayland"

require repository.inc
require dependency.inc

DEPENDS += "wayland \
            wayland-protocols \
            wayland-native"

EXTRA_OECMAKE = "-DBUILD_ELINUX_SO=ON \
                 -DBACKEND_TYPE=WAYLAND \
                 -DCMAKE_BUILD_TYPE=Release \
                 -DENABLE_ELINUX_EMBEDDER_LOG=ON \
                 -DFLUTTER_RELEASE=ON"

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${WORKDIR}/build/libflutter_elinux_wayland.so ${D}${bindir}
}

require tasks.inc
FILES:${PN}:append = "${libdir}"
