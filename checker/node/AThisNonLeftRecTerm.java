/* This file was generated by SableCC (http://www.sablecc.org/). */

package checker.node;

import checker.analysis.*;

@SuppressWarnings("nls")
public final class AThisNonLeftRecTerm extends PNonLeftRecTerm
{
    private TThis _this_;

    public AThisNonLeftRecTerm()
    {
        // Constructor
    }

    public AThisNonLeftRecTerm(
        @SuppressWarnings("hiding") TThis _this_)
    {
        // Constructor
        setThis(_this_);

    }

    @Override
    public Object clone()
    {
        return new AThisNonLeftRecTerm(
            cloneNode(this._this_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAThisNonLeftRecTerm(this);
    }

    public TThis getThis()
    {
        return this._this_;
    }

    public void setThis(TThis node)
    {
        if(this._this_ != null)
        {
            this._this_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._this_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._this_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._this_ == child)
        {
            this._this_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._this_ == oldChild)
        {
            setThis((TThis) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
