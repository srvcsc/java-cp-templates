import java.util.function.BiFunction;

class SegmentTree {
    private int[] tree;
    private int[] given;
    private int n;
    private int defaultValue;
    private BiFunction<Integer, Integer, Integer> op;

    public SegmentTree(int[] given, int defaultValue, BiFunction<Integer, Integer, Integer> op) {
        this.n = given.length;
        tree = new int[4 * n];
        this.given = given;
        this.op = op;
        this.defaultValue = defaultValue;
        build(0, 0, n - 1);
    }

    private void build(int idx, int L, int R) {
        if (L == R) {
            tree[idx] = given[L];
            return;
        }
        int mid = L + (R - L) / 2;
        build(2 * idx + 1, L, mid);
        build(2 * idx + 2, mid + 1, R);
        int lh = tree[2 * idx + 1];
        int rh = tree[2 * idx + 2];
        tree[idx] = op.apply(lh, rh);
    }

    private int query(int idx, int L, int R, int ql, int qr) {
        if (qr < L || ql > R) {
            return defaultValue;
        }
        if (L >= ql && R <= qr) {
            return tree[idx];
        }
        int mid = L + (R - L) / 2;
        int la = query(2 * idx + 1, L, mid, ql, qr);
        int ra = query(2 * idx + 2, mid + 1, R, ql, qr);
        return op.apply(la, ra);
    }

    public int query(int ql, int qr) {
        return query(0, 0, n - 1, ql, qr);
    }

    private void pointUpdate(int index, int start, int end, int targetIndex, int change) {
        if (targetIndex > end || targetIndex < start) {
            return;
        }
        if (start == end && end == targetIndex) {
            tree[index] += change;
            given[targetIndex] += change;
            return;
        }
        int mid = (start + end) / 2;
        if (targetIndex >= start && targetIndex <= mid) {
            pointUpdate(2 * index + 1, start, mid, targetIndex, change);
        } else {
            pointUpdate(2 * index + 2, mid + 1, end, targetIndex, change);
        }
        int lhs = tree[2 * index + 1];
        int rhs = tree[2 * index + 2];
        tree[index] = op.apply(lhs, rhs);
    }

    public void pointUpdate(int targetIndex, int change) {
        pointUpdate(0, 0, n - 1, targetIndex, change);
    }
}