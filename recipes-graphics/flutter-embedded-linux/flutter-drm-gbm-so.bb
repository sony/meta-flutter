SUMMARY = "Flutter embedding for eLinux with DRM-GBM backend (.so)"
DESCRIPTION = "Build .so file of the Flutter embedding for eLinux with DRM-GBM"

require repository.inc
require dependency.inc

DEPENDS += "libdrm \
            virtual/mesa \
            libinput \
            udev \
            systemd"

EXTRA_OECMAKE = "-DBUILD_ELINUX_SO=ON \
                 -DBACKEND_TYPE=DRM-GBM \
                 -DCMAKE_BUILD_TYPE=Release \
                 -DENABLE_ELINUX_EMBEDDER_LOG=ON \
                 -DFLUTTER_RELEASE=ON"

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${WORKDIR}/build/libflutter_elinux_gbm.so ${D}${bindir}
}

require tasks.inc
FILES:${PN}:append = "${libdir}"
