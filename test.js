Ut = function (a) {
    a = a.split("");
    Tt.vf(a, 26);
    Tt.uB(a, 1);
    Tt.m2(a, 76);
    Tt.uB(a, 2);
    Tt.m2(a, 21);
    Tt.uB(a, 1);
    Tt.vf(a, 45);
    Tt.m2(a, 24);
    return a.join("")
};
var Tt = {
    m2: function (a) {
        a.reverse()
    },
    vf: function (a, b) {
        var c = a[0];
        a[0] = a[b % a.length];
        a[b % a.length] = c
    },
    uB: function (a, b) {
        a.splice(0, b)
    }
};