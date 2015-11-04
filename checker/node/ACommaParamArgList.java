/* This file was generated by SableCC (http://www.sablecc.org/). */

package checker.node;

import checker.analysis.*;

@SuppressWarnings("nls")
public final class ACommaParamArgList extends PCommaParamArgList
{
    private TComma _comma_;
    private PParamArg _paramArg_;

    public ACommaParamArgList()
    {
        // Constructor
    }

    public ACommaParamArgList(
        @SuppressWarnings("hiding") TComma _comma_,
        @SuppressWarnings("hiding") PParamArg _paramArg_)
    {
        // Constructor
        setComma(_comma_);

        setParamArg(_paramArg_);

    }

    @Override
    public Object clone()
    {
        return new ACommaParamArgList(
            cloneNode(this._comma_),
            cloneNode(this._paramArg_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACommaParamArgList(this);
    }

    public TComma getComma()
    {
        return this._comma_;
    }

    public void setComma(TComma node)
    {
        if(this._comma_ != null)
        {
            this._comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._comma_ = node;
    }

    public PParamArg getParamArg()
    {
        return this._paramArg_;
    }

    public void setParamArg(PParamArg node)
    {
        if(this._paramArg_ != null)
        {
            this._paramArg_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._paramArg_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._comma_)
            + toString(this._paramArg_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._comma_ == child)
        {
            this._comma_ = null;
            return;
        }

        if(this._paramArg_ == child)
        {
            this._paramArg_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(this._paramArg_ == oldChild)
        {
            setParamArg((PParamArg) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
