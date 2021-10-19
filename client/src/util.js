/**
 * @author Quinn Tao
 * @last updated Oct 18
 */

export function getFontSize() {
    const fontSizeID = window.sessionStorage.getItem("fontSize");
    return ["16px", "18px", "20px"][fontSizeID == null ? 0 : fontSizeID];
}
export function getAccentColor() {
    const storage = window.sessionStorage;
    const accentColor = storage.getItem("accentColor");
    return accentColor == null ? "slateblue" : accentColor;
}