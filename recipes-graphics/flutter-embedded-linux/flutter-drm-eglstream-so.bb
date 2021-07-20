SUMMARY = "Flutter embedding for eLinux with DRM-EGLStream backend (.so)"
DESCRIPTION = "Build .so file of the Flutter embedding for eLinux with DRM-EGLStream"

require repository.inc
require dependency.inc

DEPENDS += "libdrm \
            virtual/mesa \
            libinput \
            udev \
            systemd"

EXTRA_OECMAKE = "-DBUILD_ELINUX_SO=ON \
                 -DBACKEND_TYPE=DRM-EGLSTREAM \
                 -DCMAKE_BUILD_TYPE=Release \
                 -DENABLE_ELINUX_EMBEDDER_LOG=ON \
                 -DFLUTTER_RELEASE=ON"

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${WORKDIR}/build/libflutter_elinux_eglstream.so ${D}${bindir}
}

require tasks.inc
