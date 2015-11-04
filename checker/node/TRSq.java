/* This file was generated by SableCC (http://www.sablecc.org/). */

package checker.node;

import checker.analysis.*;

@SuppressWarnings("nls")
public final class TRSq extends Token
{
    public TRSq()
    {
        super.setText("]");
    }

    public TRSq(int line, int pos)
    {
        super.setText("]");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TRSq(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTRSq(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TRSq text.");
    }
}