/* This file was generated by SableCC (http://www.sablecc.org/). */

package checker.node;

import checker.analysis.*;

@SuppressWarnings("nls")
public final class TExtends extends Token
{
    public TExtends()
    {
        super.setText("extends");
    }

    public TExtends(int line, int pos)
    {
        super.setText("extends");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TExtends(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTExtends(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TExtends text.");
    }
}
