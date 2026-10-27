# Impeller texture-layer platform view repro (negative result)

A small Flutter app that reproduces the *shape* of a crash I see in a real app:

- `MainActivity` is `singleTop` and shows a `PlatformViewLink` platform view.
- An invisible `ProcessTextActivity` receives `ACTION_PROCESS_TEXT` and forwards it
  to the existing `MainActivity` with `NEW_TASK | CLEAR_TOP | SINGLE_TOP`.

On a MediaTek/Mali device with Impeller (OpenGLES), the real app goes permanently
black after such an entry, with

    E flutter: EGL Error: Bad Access (12290) in .../impeller/toolkit/egl/context.cc:55
    E flutter: Could not make the context current to acquire the frame.

**This sample does not reproduce it** (16+ cycles, no EGL errors), which is why it
is published together with the issue rather than as proof.

History of the sample: it first used `AndroidView` (surface composition), then
`initExpensiveAndroidView` with a native `TextView`, then the real
`flutter_inappwebview` 6.2.0-beta.3 with `useHybridComposition: true` —
`transparentBackground: true` and `baseUrl: http://ciyue.internal/`, the same
settings the real app uses. None of the three reproduces.

## Run

    flutter pub get
    flutter build apk --release

Share any text to the app from another app's text selection menu, leave it, and
share again.
